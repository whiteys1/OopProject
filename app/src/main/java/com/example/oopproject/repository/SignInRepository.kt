package com.example.oopproject.repository

import com.example.oopproject.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun signUp(
        email : String,
        password : String,
        nickname : String,
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if ( task.isSuccessful ) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userData = User(
                        uid = uid,
                        nickname = nickname,
                        posts = listOf("defaultPostId"),
                        appliedPosts = listOf("defaultAppliedPostId")
                    )
                    database.child("users").child(uid).setValue(userData)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { error -> onError(error.message ?: "Unknown error") }
                } else {
                    onError( task.exception?.message ?: "Signup failed")
                }
            }
    }

    fun login(
        email:String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if ( task.isSuccessful ) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "로그인 실패")
                }
            }
    }
}