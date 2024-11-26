package com.example.oopproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Comment
import com.example.oopproject.repository.CommentRepository

class CommentViewModel : ViewModel() {
    private val repository = CommentRepository()

    // 기본값으로 빈 리스트를 가진 LiveData 초기화
    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData(emptyList())
    val comments: LiveData<List<Comment>> get() = _comments

    // 초기화 메서드
    fun initialize(postId: String) {
        val liveDataFromRepo = repository.observeComments(postId)
        liveDataFromRepo.observeForever { fetchedComments ->
            _comments.value = fetchedComments
        }
    }
}