package com.example.oopproject.repository

import androidx.lifecycle.MutableLiveData
import com.example.oopproject.Post
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage

class PostsRepository {
    val database = Firebase.database
    val storage = Firebase.storage
    val postRef = database.getReference("posts")
    val storageRef = storage.reference

    fun observePosts(posts: MutableLiveData<List<Post>>) {
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postsList = mutableListOf<Post>()
                for (postSnapshot in snapshot.children) {
                    val postId = postSnapshot.child("postId").getValue(String::class.java) ?: "1"
                    val name = postSnapshot.child("name").getValue(String::class.java) ?: "오류"

                    val keyword = postSnapshot.child("keyword").children.mapNotNull {
                        it.getValue(String::class.java)
                    }

                    val dueDate = postSnapshot.child("dueDate").getValue(String::class.java) ?: ""
                    val date = postSnapshot.child("date").getValue(String::class.java) ?: ""
                    val apply = postSnapshot.child("apply").getValue(String::class.java) ?: "NONE"
                    val like = postSnapshot.child("like").getValue(String::class.java) ?: "NONE"
                    val description = postSnapshot.child("description").getValue(String::class.java) ?: ""
                    val imageUrl = postSnapshot.child("imageUrl").getValue(String::class.java)

                    postsList.add(
                        Post(postId, name, keyword, dueDate, date, apply, like, description, imageUrl)
                    )
                }
                posts.postValue(postsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }


    fun updateLikeStatus(postId: String, likeStatus: String) {
        val newStatus = if (likeStatus == "LIKE") "NONE" else "LIKE"
        postRef.child("post$postId").child("like").setValue(newStatus)
    }


    fun updateApplyStatus(post:Post){
    }

}