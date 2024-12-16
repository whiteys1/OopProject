package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Comment
import com.example.oopproject.Post
import com.example.oopproject.repository.PostDetailRepository

class PostDetailViewModel : ViewModel() {
    private val repository = PostDetailRepository()

    private lateinit var _comments: LiveData<List<Comment>>
    private lateinit var _postDetail: LiveData<Post>
    private lateinit var _likeStatus: LiveData<String>
    private val _commentStatus = MutableLiveData<Boolean>()

    val comments: LiveData<List<Comment>> get() = _comments
    val postDetail: LiveData<Post> get() = _postDetail
    val likeStatus: LiveData<String> get() = _likeStatus
    val commentStatus: LiveData<Boolean> get() = _commentStatus  // 추가된 부분

    fun initialize(postId: String) {
        _comments = repository.observeComments(postId)
        _postDetail = repository.observePostDetail(postId)
        _likeStatus = repository.observeLikeStatus(postId)
    }

    fun createComment(postId: String, nickname: String, commentText: String) {
        repository.createComment(postId, nickname, commentText) { isSuccess ->
            _commentStatus.postValue(isSuccess)
        }
    }

    fun updateLikeStatus(postId: String, currentStatus: String) {
        repository.updateLikeStatus(postId, currentStatus)
    }

    fun updateApplyStatus(postId: String, apply: String) {
        repository.updateApplyStatus(postId, apply)
    }
}