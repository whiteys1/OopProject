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


class Post(val name: String,
           val keyword:Array<String>,
           val dueDate:String,
           val date: String,
           var apply: EStatus,
           var like:ELike,
           val description: String)