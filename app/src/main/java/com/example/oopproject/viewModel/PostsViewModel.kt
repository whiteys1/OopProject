package com.example.oopproject.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
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
        val filteredPosts = _posts.value?.filter { post ->
            post.apply == "APPLIED" && dateFormat.parse(post.date).after(currentDate)
        } ?: emptyList()

        val sortedPosts = filteredPosts.sortedBy { post ->
            dateFormat.parse(post.date).time
        }

        _appliedPosts.value = sortedPosts
    }

    fun filterInCalendar(selectedDate: String){
        _selectedPosts.value = _appliedPosts.value?.filter { post ->
            val compareDate = dateFormat.format(dateFormat.parse(selectedDate))
            post.date == compareDate
        } ?: _appliedPosts.value
    }

    fun filterByKeyword(keywords: List<String>) {
        _filteredPosts.value = _posts.value?.filter { post ->
            keywords.any { keyword -> post.keyword.contains(keyword) }
        } ?: emptyList()
    }

    // 단일 키워드 필터링을 위한 오버로드 함수
    fun filterByKeyword(keyword: String) {
        filterByKeyword(listOf(keyword))
    }

    fun modifyLike(post: Post) {
        val postId = post.postId
        repository.updateLikeStatus(postId, post.like)
    }

    //홈화면 재로딩 시 이전 필터링된 글 목록으로 초기화
    fun clearFilter() {
        _filteredPosts.value = _posts.value ?: emptyList()
    }
}

//페이징 시도
//    val postsFlow = Pager(
//        config = PagingConfig(
//            pageSize = 4,
//            initialLoadSize = 4,
//            enablePlaceholders = false
//        ),
//        pagingSourceFactory = { repository.PostPagingSource() }
//    ).flow.cachedIn(viewModelScope)
