package com.example.oopproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Keyword
import com.example.oopproject.repository.KeywordRepository

class KeywordViewModel() : ViewModel() {
    private val _keywords: LiveData<List<Keyword>>
    private val repository = KeywordRepository()  // Repository 인스턴스를 생성합니다.
    init{
        _keywords = repository.observeKeywords()  // Repository에서 데이터를 가져와서 LiveData에 연결합니다.
    }
    val keywords = _keywords
}