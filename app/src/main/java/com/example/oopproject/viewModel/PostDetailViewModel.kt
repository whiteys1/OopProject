package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Comment
import com.example.oopproject.Post
import com.example.oopproject.repository.PostDetailRepository

class PostDetailViewModel : ViewModel() {
    private val repository = PostDetailRepository()

    private lateinit var _comments: LiveData<List<Comment>>
    private lateinit var _postDetail: LiveData<Post>
    private lateinit var _likeStatus: LiveData<String>

    val comments: LiveData<List<Comment>> get() = _comments
    val postDetail: LiveData<Post> get() = _postDetail
    val likeStatus: LiveData<String> get() = _likeStatus

    fun initialize(postId: String) {
        _comments = repository.observeComments(postId)
        _postDetail = repository.observePostDetail(postId)
        _likeStatus = repository.observeLikeStatus(postId)
    }

    fun updateLikeStatus(postId: String, currentStatus: String) {
        repository.updateLikeStatus(postId, currentStatus)
    }
}