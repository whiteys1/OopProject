package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Post
import com.example.oopproject.repository.PostsRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostsViewModel : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _filteredPosts = MutableLiveData<List<Post>>()
    val filteredPosts: LiveData<List<Post>> = _filteredPosts

    private val _appliedPosts = MutableLiveData<List<Post>>()
    val appliedPosts: LiveData<List<Post>> = _appliedPosts

    private val _selectedPosts = MutableLiveData<Post?>()
    val selectedPosts: LiveData<Post?> = _selectedPosts

    private val repository = PostsRepository()
    init{
        repository.observePosts(_posts)
    }

    fun filterByApply(){
        val currentDate = Date()
        val filteredPosts = _posts.value?.filter { post ->
            post.apply == "APPLIED" && dateFormat.parse(post.date).after(currentDate)
        } ?: emptyList()

        val sortedPosts = filteredPosts.sortedBy { post ->
            dateFormat.parse(post.date).time
        }

        _appliedPosts.value = sortedPosts
    }

    fun filterByKeyword(selectedKeyword: String) {
        _filteredPosts.value = _posts.value?.filter { post ->
            post.keyword.contains(selectedKeyword)
        } ?: emptyList()
    }

    fun findById(postId: String){
        val post = _posts.value?.find {it.postId == postId}
        _selectedPosts.value = post
    }

    fun modifyLike(post: Post){
        val postId = post.postId
        repository.updateLikeStatus(postId, post.like)
    }

    //홈화면 재로딩 시 이전 필터링된 글 목록으로 초기화
    fun clearFilter(){
        _filteredPosts.value = _posts.value ?: emptyList()
    }
}