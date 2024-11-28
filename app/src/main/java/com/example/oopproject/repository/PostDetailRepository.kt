package com.example.oopproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oopproject.Comment
import com.example.oopproject.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostDetailRepository {
    private val database = Firebase.database.reference

    fun observeComments(postId: String): LiveData<List<Comment>> {
        val commentsLiveData = MutableLiveData<List<Comment>>()

        // posts/postId/comments 경로에서 데이터 가져오기
        database.child("posts").child("post$postId").child("comments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val commentsList = mutableListOf<Comment>()

                    for (commentSnapshot in snapshot.children) {
                        val user = commentSnapshot.child("user").getValue(String::class.java) ?: "Unknown"
                        val text = commentSnapshot.child("text").getValue(String::class.java) ?: ""
                        val createdAt = commentSnapshot.child("createdAt").getValue(String::class.java) ?: ""

                        commentsList.add(Comment(
                            nickname = user,  // user를 nickname으로 사용
                            comment = text,   // text를 comment로 사용
                            createdAt = createdAt
                        ))
                    }
                    commentsLiveData.value = commentsList
                }

                override fun onCancelled(error: DatabaseError) {
                    //Log.e("CommentRepository", "Error loading comments: ${error.message}")
                }
            })

        return commentsLiveData
    }

    fun observeLikeStatus(postId: String): LiveData<String> {
        val likeLiveData = MutableLiveData<String>()

        database.child("posts").child(postId).child("like")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val likeStatus = snapshot.getValue(String::class.java) ?: "NONE"
                    likeLiveData.value = likeStatus
                }

                override fun onCancelled(error: DatabaseError) {
                    // 에러 처리
                }
            })

        return likeLiveData
    }

    fun observePostDetail(postId: String): LiveData<Post> {
        val postDetailLiveData = MutableLiveData<Post>()

        database.child("posts").child("post$postId") // "post" + postId로 수정
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Log.d("PostDetailRepository", "Data received: ${snapshot.value}")
                    val post = Post(
                        postId = snapshot.child("postId").getValue(String::class.java) ?: "",
                        name = snapshot.child("name").getValue(String::class.java) ?: "",
                        keyword = snapshot.child("keyword").children.map {
                            it.getValue(String::class.java) ?: ""
                        },
                        dueDate = snapshot.child("dueDate").getValue(String::class.java) ?: "",
                        date = snapshot.child("date").getValue(String::class.java) ?: "",
                        apply = snapshot.child("apply").getValue(String::class.java) ?: "NONE",
                        like = snapshot.child("like").getValue(String::class.java) ?: "NONE",
                        description = snapshot.child("description").getValue(String::class.java) ?: ""
                    )
                    //Log.d("PostDetailRepository", "Mapped Post: $post")
                    postDetailLiveData.value = post
                }

                override fun onCancelled(error: DatabaseError) {
                    //Log.e("PostDetailRepository", "Error loading post: ${error.message}")
                }
            })

        return postDetailLiveData
    }

    fun updateLikeStatus(postId: String, currentStatus: String) {
        val newStatus = if (currentStatus == "LIKE") "NONE" else "LIKE"
        database.child("posts").child(postId).child("like").setValue(newStatus)
    }
}