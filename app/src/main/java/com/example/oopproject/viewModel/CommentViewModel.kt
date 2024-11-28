package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Comment
import com.example.oopproject.repository.PostDetailRepository

class CommentViewModel : ViewModel() {
    private val repository = PostDetailRepository() // Repository를 직접 생성
    private lateinit var _comments: LiveData<List<Comment>> // LiveData 선언
    val comments: LiveData<List<Comment>> get() = _comments

    // 초기화 시 Repository에서 LiveData 연결
    fun initialize(postId: String) {
        _comments = repository.observeComments(postId)
    }
}