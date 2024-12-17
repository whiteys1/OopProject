package com.example.oopproject

data class User (
    val uid: String = "",
    val nickname: String = "",
    val gender : String = "NONE",
    val profileImageUrl: String = "NONE",
    val posts: List<String> = emptyList(),
    val appliedPosts : List<String> = emptyList()
)