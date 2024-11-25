package com.example.oopproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oopproject.Comment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CommentRepository {
    private val database = Firebase.database.reference

    fun observeComments(postId: String): LiveData<List<Comment>> {
        val commentsLiveData = MutableLiveData<List<Comment>>()

        // posts/postId/comments 경로에서 데이터 가져오기
        database.child("posts").child(postId).child("comments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val commentsList = mutableListOf<Comment>()

                    for (commentSnapshot in snapshot.children) {
                        val nickname = commentSnapshot.child("nickname").getValue(String::class.java) ?: "Unknown"
                        val comment = commentSnapshot.child("comment").getValue(String::class.java) ?: ""
                        val createdAt = commentSnapshot.child("createdAt").getValue(String::class.java) ?: ""

                        commentsList.add(Comment(nickname, comment, createdAt))
                    }
                    commentsLiveData.value = commentsList
                }

                override fun onCancelled(error: DatabaseError) {
                    //Log.e("CommentRepository", "Error loading comments: ${error.message}")
                }
            })

        return commentsLiveData
    }
}