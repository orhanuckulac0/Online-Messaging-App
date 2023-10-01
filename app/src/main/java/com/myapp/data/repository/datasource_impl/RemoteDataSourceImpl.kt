package com.myapp.data.repository.datasource_impl

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.myapp.data.repository.data_source.RemoteDataSource
import com.myapp.presentation.util.ResultHappen
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.myapp.data.model.UserModel
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
    ) : RemoteDataSource {

    override suspend fun getCurrentUserDetails(): HashMap<String, String?> {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        val userDetails = HashMap<String, String?>()

        if (currentUser != null) {
            val currentUserId = currentUser.uid

            val userDocRef = db
                .collection("users")
                .document(currentUserId)

            try {
                val documentSnapshot = userDocRef.get().await()

                if (documentSnapshot.exists()) {
                    userDetails["email"] = documentSnapshot.getString("email")
                    userDetails["name"] = documentSnapshot.getString("name")
                    userDetails["surname"] = documentSnapshot.getString("surname")
                    userDetails["profileImage"] = documentSnapshot.getString("profileImage")
                }
            } catch (e: Exception) {
                // Handle the exception, e.g., log or rethrow
            }
        }
        return userDetails
    }
    override suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                userModel.email,
                userModel.password!!
            ).await()
            val user = authResult.user

            if (user != null) {
                val userData = hashMapOf(
                    "id" to user.uid,
                    "email" to userModel.email,
                    "password" to userModel.password,
                    "name" to userModel.name,
                    "surname" to userModel.surname,
                    "profileImage" to userModel.profileImage
                )

                firestore.collection("users").document(user.uid)
                    .set(userData).await()

                ResultHappen.Success(user)
            } else {
                ResultHappen.Error("User registration failed")
            }
        } catch (e: Exception) {
            ResultHappen.Error("Error during user registration: ${e.message}")
        }
    }

    override suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                it.result
            }

            if (authResult.isSuccessful) {
                ResultHappen.Success(authResult.result.user)
            } else {
                when (authResult.exception) {
                    is FirebaseAuthInvalidUserException -> {
                        ResultHappen.Error("User with this email does not exist.")
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        ResultHappen.Error("Invalid email or password format.")
                    }
                    else -> {
                        ResultHappen.Error("An error occurred during login, try again.")
                    }
                }
            }
        } catch (e: Exception) {
            ResultHappen.Error("An error occurred during login: ${e.message}")
        }
    }

    override suspend fun updateUserDetails(
        name: String,
        surname: String,
        email: String,
        profileImage: String
    ): ResultHappen<Unit> {
        val currentUser = firebaseAuth.currentUser?.uid
        val documentPath = "users/$currentUser"
        val updatedData = mapOf(
            "name" to name,
            "surname" to surname,
            "email" to email,
            "profileImage" to profileImage
        )

        return try {
            firestore.document(documentPath)
                .update(updatedData)
                .await()
            ResultHappen.Success(Unit)
        } catch (e: Exception) {
            ResultHappen.Error("Error updating user details: ${e.message}")
        }
    }
}