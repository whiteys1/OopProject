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

    fun observePosts(posts: MutableLiveData<List<Post>>) {
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 여러 post 데이터를 리스트로 변환하여 바로 LiveData에 반영
                val postsList = snapshot.children.mapNotNull { it.getValue(Post::class.java) }
                posts.postValue(postsList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getPostByName(postName: String): MutableLiveData<Post?>{
        val postLiveData = MutableLiveData<Post?>()

        postRef.orderByChild("name").equalTo(postName).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children.firstOrNull()?.getValue(Post::class.java)
                postLiveData.postValue(post)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return postLiveData
    }

    fun updateLikeStatus(post: Post, newLikeStatus: String) {
        postRef.orderByChild("name").equalTo(post.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postSnapshot = snapshot.children.firstOrNull()
                    if (postSnapshot != null) {
                        postSnapshot.ref.child("like").setValue(newLikeStatus)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun updateApplyStatus(post:Post){
    }

}