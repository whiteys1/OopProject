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

    private val _selectedPosts = MutableLiveData<List<Post>>()
    val selectedPosts: LiveData<List<Post>> = _selectedPosts

    private val repository = PostsRepository()

    init {
        repository.observePosts(_posts)
    }

    fun filterByApply() {
        val currentDate = Date()
        val filteredPosts = _posts.value?.filter { post -> post.apply == "APPLIED" && dateFormat.parse(post.date).after(currentDate) } ?: emptyList()

        val sortedPosts = filteredPosts.sortedBy { post -> dateFormat.parse(post.date).time }
        _appliedPosts.value = sortedPosts
    }

    fun filterInCalendar(selectedDate: String){
        val compareDate = dateFormat.format(dateFormat.parse(selectedDate))
        _selectedPosts.value = _appliedPosts.value?.filter { post -> post.date == compareDate } ?: _appliedPosts.value
    }

    fun filterByKeyword(keywords: List<String>) {
        _filteredPosts.value = _posts.value?.filter { post ->
            keywords.any { keyword -> post.keyword.contains(keyword) } } ?: emptyList()
    }

    fun filterByKeyword(keyword: String) {
        filterByKeyword(listOf(keyword))
    }

    fun modifyLike(post: Post) {
        val postId = post.postId
        repository.updateLikeStatus(postId, post.like)
        if (_filteredPosts.value?.isNotEmpty() == true) {
            _filteredPosts.value = _filteredPosts.value?.map {
                if (it.postId == postId) it.copy(like = if (post.like == "LIKE") "NONE" else "LIKE") else it
            }
        }
    }

    //목록 초기화
    fun clearFilter() {
        _filteredPosts.value = _posts.value ?: emptyList()
    }
}