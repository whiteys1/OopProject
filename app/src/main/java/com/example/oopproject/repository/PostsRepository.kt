package com.example.oopproject.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.example.oopproject.Post
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await
import androidx.paging.PagingState
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import kotlinx.coroutines.tasks.await

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

//페이징 시도
//inner class PostPagingSource : PagingSource<String, Post>(){
//        override suspend fun load(params: LoadParams<String>) : LoadResult<String, Post>{
//            try{
//                Log.d("Paging", "Loading page with key: ${params.key}") // 로딩 중인 페이지 번호 출력
//                val query = if (params.key == null){ //첫번째 페이지(요청값이 null일 수밖에 없음)
//                    postRef.orderByKey().limitToFirst(params.loadSize)
//                } else {
//                    postRef.orderByKey().startAfter(params.key).limitToFirst(params.loadSize)
//                }
//                val snapshot = query.get().await()
//                val posts = snapshot.children.mapNotNull { it.getValue(Post::class.java) }
//                Log.d("Paging", "Loaded ${posts.size} posts")  // 로드된 데이터의 크기 출력
//                return LoadResult.Page(
//                    data = posts,
//                    prevKey = null,
//                    nextKey = snapshot.children.lastOrNull()?.key
//                )
//            } catch (e: Exception) {
//                // 예외 발생 시 LoadResult.Error 반환
//                return LoadResult.Error(e)
//                Log.e("Paging", "Error loading page: ${e.message}") // 오류 로그
//            }
//        }
//
//        override fun getRefreshKey(state: PagingState<String, Post>): String? {
//            val position = state.anchorPosition ?: return null
//            val closestPage = state.closestPageToPosition(position)
//            return closestPage?.nextKey
//        }
//
//    }