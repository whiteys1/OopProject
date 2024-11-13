package com.example.oopproject

enum class ELike{
    LIKE,
    NONE
}
enum class EGen{
    MALE,
    FEMALE,
    COMPANY
}

enum class EStatus{
    APPLIED,
    NONE
}


data class Post(
    val name: String = "",
    val keyword: List<String> = listOf(),
    val dueDate: String = "",
    val date: String = "",
    var apply: String = "NONE",  // Enum 대신 문자열로 변경
    var like: String = "NONE",   // Enum 대신 문자열로 변경
    val description: String = ""
)