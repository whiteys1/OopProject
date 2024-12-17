package com.example.oopproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.Keyword
import com.example.oopproject.repository.KeywordRepository

class KeywordViewModel() : ViewModel() {
    private val _keywords: LiveData<List<Keyword>> // 캡슐화
    private val repository = KeywordRepository()
    init{
        // 초기화 시 Repository에서 observeKeywords()를 호출하여 LiveData(키워드)를 가져옴
        _keywords = repository.observeKeywords()
    }
    val keywords = _keywords
}