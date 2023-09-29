package com.myapp.presentation.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class StorageUtil {

    companion object {

        fun uploadToStorage(uri: Uri, context: Context) {
            val storage = FirebaseStorage.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            val storageRef = storage.reference
            val uid = user?.uid

            if (uid != null) {
                val spaceRef: StorageReference = storageRef.child("images/$uid.jpg")

                val byteArray: ByteArray? = context.contentResolver
                    .openInputStream(uri)
                    ?.use { it.readBytes() }

                byteArray?.let {
                    val uploadTask: UploadTask = spaceRef.putBytes(byteArray)

                    uploadTask.addOnFailureListener { exception ->
                        Toast.makeText(
                            context,
                            "Upload Failed, Try Again",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.e("MYTAG", "Upload failed: ${exception.message}")
                    }.addOnSuccessListener {
                        Log.i("MYTAG", "UPLOADED TO STORAGE SUCCESS")
                    }
                }
            } else {
                Log.e("MYTAG", "User is not authenticated")
            }
        }
    }
}