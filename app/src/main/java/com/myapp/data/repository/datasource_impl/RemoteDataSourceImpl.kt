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
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.myapp.data.model.PushNotification
import com.myapp.data.model.UserModel
import com.myapp.data.model.UserModelFirestore
import com.myapp.data.repository.fcm.RetrofitInstance
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging,
    private val retrofitInstance: RetrofitInstance
    ) : RemoteDataSource {

    override suspend fun getCurrentUserDetails(): ResultHappen<UserModel> {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val currentUserId = currentUser.uid

            val userDocRef = firestore
                .collection("users")
                .document(currentUserId)

            try {
                val documentSnapshot = userDocRef.get().await()

                if (documentSnapshot.exists()) {

                    val id = documentSnapshot.getString("id")
                    val name = documentSnapshot.getString("name")
                    val surname = documentSnapshot.getString("surname")
                    val email = documentSnapshot.getString("email")
                    val profileImage = documentSnapshot.getString("profileImage")
                    val loggedIn = documentSnapshot.getBoolean("loggedIn")
                    val fcmToken = documentSnapshot.getString("fcmToken")

                    val userDetails = UserModel(
                        id = id,
                        name = name ?: "",
                        surname = surname ?: "",
                        email = email ?: "",
                        password = null,
                        profileImage = profileImage ?: "",
                        loggedIn = loggedIn ?: false,
                        fcmToken = fcmToken ?: ""
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
    }
    override suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                userModel.email,
                userModel.password!!
            ).await()
            val user = authResult.user

            if (user != null) {
                val fcmToken = firebaseMessaging.token.await()
                Log.i("MYTAG", fcmToken)
                val userData = hashMapOf(
                    "id" to user.uid,
                    "email" to userModel.email,
                    "password" to userModel.password,
                    "name" to userModel.name,
                    "surname" to userModel.surname,
                    "profileImage" to userModel.profileImage,
                    "loggedIn" to userModel.loggedIn,
                    "fcmToken" to fcmToken
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
            val authResultTask = firebaseAuth.signInWithEmailAndPassword(email, password)
            val authResult = authResultTask.await()

            if (authResult.user != null) {
                try {
                    // set user login status to true
                    val user = firebaseAuth.currentUser?.uid
                    firestore
                        .document("users/$user")
                        .update("loggedIn", true)
                }catch (e: java.lang.Exception){
                    Log.i("MYTAG", "${e.message}")
                }
                ResultHappen.Success(authResult.user)
            } else {
                val exception = authResultTask.exception
                Log.e("MYTAG", "Login failed: ${exception?.message}")
                when (exception) {
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
            Log.e("MYTAG", "Login error: ${e.message}")
            ResultHappen.Error("An error occurred during login, try again.")
        }
    }

    override suspend fun updateUserDetails(userModel: UserModel): ResultHappen<Unit> {
        val currentUser = firebaseAuth.currentUser?.uid
        val documentPath = "users/$currentUser"
        val updatedData = mapOf(
            "name" to userModel.name,
            "surname" to userModel.surname,
            "email" to userModel.email,
            "profileImage" to userModel.profileImage,
            "loggedIn" to userModel.loggedIn
        )

        return try {
            firestore
                .document(documentPath)
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

    override suspend fun getOnlineUsers(): ResultHappen<List<UserModelFirestore>> {
        try {
            val usersCollection = firestore.collection("users")

            val querySnapshot = usersCollection.whereEqualTo("loggedIn", true).get().await()

            val loggedInUsers = mutableListOf<UserModelFirestore>()
            for (document in querySnapshot.documents) {
                val user = document.toObject(UserModelFirestore::class.java)
                if (user != null) {
                    user.id = document.id // Set the document ID in the UserModel
                    loggedInUsers.add(user)
                }
            }

            return ResultHappen.Success(loggedInUsers)
        } catch (e: Exception) {
            return ResultHappen.Error("Failed to retrieve logged-in users: ${e.message}")
        }
    }

    override suspend fun addFriendsAndSendNotification(
        currentUserID: String,
        email: String,
        pushNotification: PushNotification
    ): ResultHappen<Unit> {
        try {
            val usersCollection = firestore.collection("users")
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()

            if (!querySnapshot.isEmpty) {
                val targetUserDocument = querySnapshot.documents.first()
                val targetUserId = targetUserDocument.id
                val targetUserFCMToken = targetUserDocument.data?.get("fcmToken")

                // Send a friend request
                val friendRequestData = mapOf(
                    "senderId" to currentUserID, // ID of the sender
                    "status" to "pending", // Status of the request (e.g., "pending")
                    "targetId" to targetUserId,
                    "targetFCMToken" to targetUserFCMToken
                    // Other data you want to include
                )

                firestore.collection("friend_requests")
                    .document(targetUserId)
                    .set(friendRequestData, SetOptions.merge()).await()

                // Send a notification to the target user
                sendNotification(pushNotification)

                Log.i("MYTAG", "${Unit}")
                return ResultHappen.Success(Unit)
            } else {
                Log.i("MYTAG","User not found with email: $email")
                return ResultHappen.Error("User not found with email: $email")
            }
        } catch (e: Exception) {
            return ResultHappen.Error("Error adding friend: ${e.message}")
        }
    }

    private suspend fun sendNotification(pushNotification: PushNotification) {
        try {
            val response = retrofitInstance.api.postNotification(pushNotification)
            if(response.isSuccessful) {
                Log.i("MYTAG", "Response: ${response.isSuccessful}")
            } else {
                Log.i("MYTAG", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.i("MYTAG", e.toString())
        }
    }
}