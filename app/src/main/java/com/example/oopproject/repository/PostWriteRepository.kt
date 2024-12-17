package com.example.oopproject.repository

import android.net.Uri
import android.provider.ContactsContract.Data
import com.example.oopproject.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class PostWriteRepository {

    private val database : DatabaseReference = FirebaseDatabase.getInstance().reference
    private val postRef: DatabaseReference = database.child("posts")
    private val storage = FirebaseStorage.getInstance()

    fun uploadImage(imageUri : Uri, onComplete: (String?) -> Unit) {
        val storageRef = storage.reference.child("images/${UUID.randomUUID()}.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onComplete(uri.toString())
                }.addOnFailureListener {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun createPost(initialPost: Post, onComplete: (Boolean) -> Unit) {

        postRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val lastKey = dataSnapshot?.children?.mapNotNull { it.key }
                    ?.filter { it.startsWith("post") }
                    ?.mapNotNull { it.removePrefix("post").toIntOrNull() }
                    ?.maxOrNull() ?: 0

                val nextKey = lastKey + 1
                val postKey = "post$nextKey"

                val postAddedId = initialPost.copy(postId = nextKey.toString())

                postRef.child(postKey).setValue(postAddedId)
                    .addOnCompleteListener { task ->
                        onComplete(task.isSuccessful)
                    }
            } else {
                onComplete(false)
            }
        }
    }
}