package com.example.oopproject.repository

import android.provider.ContactsContract.Data
import com.example.oopproject.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostWriteRepository {

    private val database : DatabaseReference = FirebaseDatabase.getInstance().reference
    private val postRef: DatabaseReference = database.child("posts")

    fun createPost(post: Post, onComplete: (Boolean) -> Unit) {

        postRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val lastKey = dataSnapshot?.children?.mapNotNull { it.key }
                    ?.filter { it.startsWith("post") }
                    ?.mapNotNull { it.removePrefix("post").toIntOrNull() }
                    ?.maxOrNull() ?: 0

                val nextKey = "post${lastKey + 1}"

                postRef.child(nextKey).setValue(post)
                    .addOnCompleteListener { task ->
                        onComplete(task.isSuccessful)
                    }
            } else {
                onComplete(false)
            }
        }
    }
}