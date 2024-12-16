package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Post
import com.example.oopproject.repository.PostWriteRepository
import com.google.android.gms.maps.model.LatLng

class PostWriteViewModel : ViewModel() {
    private val repository = PostWriteRepository()

    private val _isPostCreated = MutableLiveData<Boolean>()
    val isPostCreated : LiveData<Boolean> get() = _isPostCreated

    private val _selectedLatLng = MutableLiveData<LatLng?>()
    val selectedLatLng: LiveData<LatLng?> get() = _selectedLatLng

    fun setSelectedLatLng(latLng : LatLng) {
        _selectedLatLng.postValue(latLng)
    }

    fun createPost(post: Post) {
        repository.createPost(post) { isSuccess ->
            _isPostCreated.postValue(isSuccess)
        }

    }
}