package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Post
import com.example.oopproject.repository.PostWriteRepository

class PostWriteViewModel : ViewModel() {
    private val repository = PostWriteRepository()

    private val _isPostCreated = MutableLiveData<Boolean>()
    val isPostCreated : LiveData<Boolean> get() = _isPostCreated

    fun createPost(post: Post) {
        repository.createPost(post) { isSuccess ->
            _isPostCreated.postValue(isSuccess)
        }

    }
}