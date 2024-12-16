package com.example.oopproject

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class EGen{
    MALE,
    FEMALE,
    COMPANY
}

data class Post(
    val postId: String = "",
    val name: String = "",
    val keyword: List<String> = listOf(),
    val dueDate: String = "",
    val date: String = "",
    var apply: String = "NONE",
    var like: String = "NONE",
    val description: String = "",
    val imageUrl: String? = "",
    val latitude : Double? = 0.0,
    val longitude : Double? = 0.0
){
    fun isApplied() = apply == "APPLIED"
    fun isDueDatePassed(currentDate: Date): Boolean{
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return dateFormat.parse(date).after(currentDate)
    }
}