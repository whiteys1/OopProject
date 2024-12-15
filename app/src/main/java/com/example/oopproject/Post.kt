package com.example.oopproject

//enum class ELike{
//    LIKE,
//    NONE
//}

enum class EGen{
    MALE,
    FEMALE,
    COMPANY
}

//enum class EStatus{
//    APPLIED,
//    NONE
//}


data class Post(
    val postId: String = "",
    val name: String = "",
    val keyword: List<String> = listOf(),
    val dueDate: String = "",
    val date: String = "",
    var apply: String = "NONE",
    var like: String = "NONE",
    val description: String = "",
    val imageUrl: String? = ""
)