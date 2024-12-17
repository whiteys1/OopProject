package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Comment
import com.example.oopproject.Post
import com.example.oopproject.repository.PostDetailRepository

class PostDetailViewModel : ViewModel() {
    private val repository = PostDetailRepository()

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _postDetail = MutableLiveData<Post>()
    val postDetail: LiveData<Post> get() = _postDetail

    private val _likeStatus = MutableLiveData<String>()
    val likeStatus: LiveData<String> get() = _likeStatus

    private val _commentStatus = MutableLiveData<Boolean>()
    val commentStatus: LiveData<Boolean> get() = _commentStatus

    fun initialize(postId: String) {
        repository.observeComments(postId).observeForever { newComments ->
            _comments.postValue(newComments)
        }

        repository.observePostDetail(postId).observeForever { newPostDetail ->
            _postDetail.postValue(newPostDetail)
        }

        repository.observeLikeStatus(postId).observeForever { newLikeStatus ->
            _likeStatus.postValue(newLikeStatus)
        }
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