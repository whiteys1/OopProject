package com.example.oopproject.repository

import android.util.Log
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

                    for (commentSnapshot in snapshot.children) { //디비 댓글 수 만큼 반복문
                        //db의 내용을 가져옴
                        val nickname = commentSnapshot.child("nickname").getValue(String::class.java) ?: "Unknown"
                        val comment = commentSnapshot.child("comment").getValue(String::class.java) ?: ""
                        val createdAt = commentSnapshot.child("createdAt").getValue(String::class.java) ?: ""

                        commentsList.add(Comment( //가져온 정보를 리스트에 추가,
                            nickname = nickname,
                            comment = comment,
                            createdAt = createdAt
                        ))
                    }
                    commentsLiveData.value = commentsList
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        return commentsLiveData
    }

    fun observeLikeStatus(postId: String): LiveData<String> {
        val likeLiveData = MutableLiveData<String>()

        database.child("posts").child("post$postId").child("like")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val likeStatus = snapshot.getValue(String::class.java) ?: "NONE"
                    likeLiveData.value = likeStatus
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        return likeLiveData
    }

    fun observePostDetail(postId: String): LiveData<Post> {
        val postDetailLiveData = MutableLiveData<Post>()

        database.child("posts").child("post$postId") // "post" + postId로 수정
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
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
                    postDetailLiveData.value = post
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        return postDetailLiveData
    }

    fun updateLikeStatus(postId: String, currentStatus: String) {
        val newStatus = if (currentStatus == "LIKE") "NONE" else "LIKE"
        database.child("posts").child("post$postId").child("like").setValue(newStatus)
    }

    fun updateApplyStatus(postId: String, currentStatus: String) {
        val newStatus = if (currentStatus == "NONE") "APPLIED" else "NONE"
        database.child("posts").child("post$postId").child("apply").setValue(newStatus)
    }
}