package com.myapp.presentation.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseAuthManager {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registerUser(email: String, password: String, onComplete: (FirebaseUser?) -> Unit, onError: (String) -> Unit) {

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
                            "password" to password
                            // Add more user data as needed
                        )

                        userDocument.set(userData)
                            .addOnSuccessListener {
                                onComplete.invoke(user)
                            }
                            .addOnFailureListener { e ->
                                onError.invoke("Error creating user document: ${e.message}")
                                Log.i("MYTAG","Error creating user document: ${e.message}")
                            }
                    }
                } else {
                    onError.invoke("Registration failed: ${authResult.exception?.message}")
                    Log.i("MYTAG","Error creating user document: ${authResult.exception?.message}")

                }
            }
    }

    fun loginUser(email: String, password: String, onComplete: (FirebaseUser?) -> Unit, onError: (String) -> Unit){

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult->
                if (authResult.isSuccessful){
                    onComplete.invoke(authResult.result?.user)
                }else {
                    onError.invoke("Login failed: ${authResult.exception?.message}")
                }
            }
    }
}
