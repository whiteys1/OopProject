package com.example.oopproject.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.oopproject.Post
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class PostsRepository {
    val database = Firebase.database
    val postRef = database.getReference("posts")

    fun observePosts(posts: MutableLiveData<List<Post>>) {
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postsList = mutableListOf<Post>()
                for (postSnapshot in snapshot.children) {
                    val postId = postSnapshot.child("postId").getValue(String::class.java) ?: "1"
                    val name = postSnapshot.child("name").getValue(String::class.java) ?: "오류"

                    val keyword = postSnapshot.child("keyword").children.mapNotNull {
                        it.getValue(String::class.java) // 각 리스트 요소를 String으로 변환
                    }

                    val dueDate = postSnapshot.child("dueDate").getValue(String::class.java) ?: ""
                    val date = postSnapshot.child("date").getValue(String::class.java) ?: ""
                    val apply = postSnapshot.child("apply").getValue(String::class.java) ?: "NONE"
                    val like = postSnapshot.child("like").getValue(String::class.java) ?: "NONE"
                    val description = postSnapshot.child("description").getValue(String::class.java) ?: ""
                    postsList.add(Post(postId, name, keyword, dueDate, date, apply, like, description))
                }

                posts.postValue(postsList)
            }

            override fun onCancelled(error: DatabaseError) {
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