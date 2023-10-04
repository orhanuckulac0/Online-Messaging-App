package com.myapp.data.repository.datasource_impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.myapp.data.repository.data_source.RemoteDataSource
import com.myapp.presentation.util.ResultHappen
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.myapp.data.model.UserModel
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
    ) : RemoteDataSource {

    override suspend fun getCurrentUserDetails(): ResultHappen<UserModel> {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val currentUserId = currentUser.uid

            val userDocRef = db
                .collection("users")
                .document(currentUserId)

            try {
                val documentSnapshot = userDocRef.get().await()

                if (documentSnapshot.exists()) {

                    val name = documentSnapshot.getString("name")
                    val surname = documentSnapshot.getString("surname")
                    val email = documentSnapshot.getString("email")
                    val profileImage = documentSnapshot.getString("profileImage")

                    val userDetails = UserModel(
                        id = null,
                        name = name ?: "",
                        surname = surname ?: "",
                        email = email ?: "",
                        password = null,
                        profileImage = profileImage ?: ""
                    )

                    return ResultHappen.Success(userDetails)
                } else {
                    return ResultHappen.Error("User document does not exist")
                }
            } catch (e: Exception) {
                return ResultHappen.Error("Error fetching user details: ${e.message}")
            }
        } else {
            return ResultHappen.Error("User is not authenticated")
        }


    }    override suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?> {
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

    override suspend fun updateUserDetails(userModel: UserModel): ResultHappen<Unit> {
        val currentUser = firebaseAuth.currentUser?.uid
        val documentPath = "users/$currentUser"
        val updatedData = mapOf(
            "name" to userModel.name,
            "surname" to userModel.surname,
            "email" to userModel.email,
            "profileImage" to userModel.profileImage
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

    override suspend fun getImageURLFromStorage(uri: Uri, context: Context): ResultHappen<String?> {
        val storage = FirebaseStorage.getInstance()
        val user = firebaseAuth.currentUser
        val storageRef = storage.reference
        val uid = user?.uid

        if (uid != null) {
            val spaceRef: StorageReference = storageRef.child("images/$uid.jpg")

            val byteArray: ByteArray? = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }

            return byteArray?.let {
                val uploadTask: UploadTask = spaceRef.putBytes(byteArray)

                try {
                    uploadTask.await() // Wait for the upload to complete
                    val downloadUrl = spaceRef.downloadUrl.await()
                    Log.i("MYTAG", "Uploaded to Storage, URL: $downloadUrl")
                    ResultHappen.Success(downloadUrl.toString()) // Return the download URL as a string
                } catch (e: Exception) {
                    Log.e("MYTAG", "Upload failed: ${e.message}", e)
                    ResultHappen.Error(e.message.toString()) // Return an error result
                }
            } ?: ResultHappen.Error("byteArray is null")
        } else {
            Log.e("MYTAG", "User is not authenticated")
            return ResultHappen.Error("User is not authenticated")
        }
    }
}