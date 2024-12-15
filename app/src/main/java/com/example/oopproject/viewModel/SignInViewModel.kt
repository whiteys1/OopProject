package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.repository.SignInRepository

class SignInViewModel : ViewModel() {
    private val repository = SignInRepository()

    private val _signUpStatus = MutableLiveData<Result<Unit>>()
    val signUpStatus : LiveData<Result<Unit>> = _signUpStatus

    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> get() = _loginStatus

    fun signUp(email: String, password : String, nickname : String) {
        repository.signUp(
            email = email,
            password = password,
            nickname = nickname,
            onSuccess = { _signUpStatus.value = Result.success(Unit) },
            onError = { error -> _signUpStatus.value = Result.failure(Exception(error))}
        )
    }

    fun login(email: String, password: String) {
        repository.login(
            email,
            password,
            onSuccess = { _loginStatus.value = "로그인 성공" },
            onError = { error -> _loginStatus.value = error }
        )
    }

}