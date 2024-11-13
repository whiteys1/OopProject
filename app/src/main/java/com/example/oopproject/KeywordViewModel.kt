package com.example.oopproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oopproject.repository.KeywordRepository

class KeywordViewModel() : ViewModel() {
    private val repository = KeywordRepository()  // Repository 인스턴스를 생성합니다.
    val keywords: LiveData<List<Keyword>> = repository.fetchKeywords()  // Repository에서 데이터를 가져와서 LiveData에 연결합니다.
}
