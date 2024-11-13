package com.example.oopproject.repository

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

    fun observePosts(posts: MutableLiveData<List<Post>>){
        postRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val postsList = mutableListOf<Post>()
                // 여러 post 데이터를 리스트로 변환하여 담기
                snapshot.children.forEach { postSnapshot ->
                    val post = postSnapshot.getValue(Post::class.java)
                    if (post != null) {
                        postsList.add(post)
                    }
                }
                // 리스트를 LiveData에 반영
                posts.postValue(postsList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun updateLikeStatus(post: Post) {
        postRef.orderByChild("name").equalTo(post.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val currentPost = postSnapshot.getValue(Post::class.java)
                        if (currentPost != null && currentPost.name == post.name) {
                            val newLikeStatus = if (currentPost.like == "LIKE") "NONE" else "LIKE"
                            postSnapshot.ref.child("like").setValue(newLikeStatus)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
//    fun updateLikeStatus(post: Post) {
//        val newLikeStatus = if (post.like == "LIKE") "NONE" else "LIKE"

//        postRef.child(post.name).child("like").setValue(newLikeStatus)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    post.like = newLikeStatus
//                }
//            }
//    }
}