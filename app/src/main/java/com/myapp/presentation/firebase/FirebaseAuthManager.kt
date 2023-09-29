package com.myapp.presentation.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseAuthManager {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registerUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        profileImage: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    // Registration successful, get the user's UID
                    val user = authResult.result?.user
                    val uid = user?.uid

                    // Create a Firestore document for the user
                    if (uid != null) {
                        val userDocument = firestore.collection("users").document(uid)

                        val userData = hashMapOf(
                            "id" to user.uid,
                            "email" to email,
                            "password" to password,
                            "name" to name,
                            "surname" to surname,
                            "profileImage" to profileImage
                            // Add more user data as needed
                        )

                        userDocument.set(userData)
                            .addOnSuccessListener {
                                onComplete.invoke(user)
                            }
                            .addOnFailureListener { e ->
                                onError.invoke("Error creating user document: ${e.message}")
                                Log.i("MYTAG", "Error creating user document: ${e.message}")
                            }
                    }
                } else {
                    onError.invoke("Registration failed: ${authResult.exception?.message}")
                    Log.i("MYTAG", "Error creating user document: ${authResult.exception?.message}")
                }
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    onComplete.invoke(authResult.result?.user)
                } else {
                    when (authResult.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            val errorMessage = "User with this email does not exist."
                            onError.invoke(errorMessage)
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            val errorMessage = "Invalid email or password format."
                            onError.invoke(errorMessage)
                        }

                        else -> {
                            val errorMessage = "An error occurred during login, try again."
                            onError.invoke(errorMessage)
                        }
                    }
                    Log.i("MYTAG", "${authResult.exception?.message}")
                }
            }
    }

    fun getCurrentUserDetails(callback: (HashMap<String, String?>) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val currentUserId = currentUser.uid

            val userDocRef = db
                .collection("users")
                .document(currentUserId)

            // Fetch the user's document
            userDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val userDetails = HashMap<String, String?>()

                    if (documentSnapshot.exists()) {

                        userDetails["email"] = documentSnapshot.getString("email")
                        userDetails["name"] = documentSnapshot.getString("name")
                        userDetails["surname"] = documentSnapshot.getString("surname")
                        userDetails["profileImage"] = documentSnapshot.getString("profileImage")

                        callback(userDetails)
                    } else {
                        callback(userDetails)
                    }
                }
                .addOnFailureListener { exception ->
                    val userDetails = HashMap<String, String?>()
                    callback(userDetails)
                }
        } else {
            val userDetails = HashMap<String, String?>()
            callback(userDetails)
        }
    }

    fun updateUserDetails(
        name: String,
        surname: String,
        email: String,
        profileImage: String
    ){
        val db = FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth.currentUser?.uid
        val documentPath = "users/$currentUser"
        val updatedData = mapOf(
            "name" to name,
            "surname" to surname,
            "email" to email,
            "profileImage" to profileImage,
        )

        // Update the document
        db.document(documentPath)
            .update(updatedData)
            .addOnSuccessListener {
                // TODO finish up here
            }
            .addOnFailureListener { e ->
                Log.i("MYTAG", e.message.toString())
            }

    }
}
