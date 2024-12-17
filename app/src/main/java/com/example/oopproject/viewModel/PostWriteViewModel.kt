package com.example.oopproject.viewModel

import android.net.Uri
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

    fun uploadImageAndCreatPost(imageUri : Uri, post: Post) {
        repository.uploadImage(imageUri) { imageUrl ->
            if (imageUrl != null) {
                val postWithImage = post.copy(imageUrl = imageUrl)
                createPost(postWithImage)
            } else {
                _isPostCreated.postValue(false)
            }
        }
    }

    fun createPost(post: Post) {
        repository.createPost(post) { isSuccess ->
            _isPostCreated.postValue(isSuccess)
        }

    }
}