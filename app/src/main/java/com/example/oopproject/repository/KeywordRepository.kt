package com.example.oopproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oopproject.Keyword
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class KeywordRepository {
    private val database = Firebase.database  // Firebase 데이터베이스 인스턴스
    private val keywordsRef = database.getReference("keywords")  // 레퍼런스를 가져옴

    fun observeKeywords(): LiveData<List<Keyword>> { // viewmodel에 전달
        val liveData = MutableLiveData<List<Keyword>>()  // 데이터를 담을 LiveData를 생성

        // 데이터베이스에서 데이터 변경을 리스닝합니다.
        keywordsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {// 파베에서 데이터를 읽을 때 제공되는 데이터 객체로 파이어베이스의 데이터를 가져와서 사용하게 해줌
                val keywords = mutableListOf<Keyword>()  // 데이터를 저장할 리스트를 생성 데이터 수정을 위해 mutableList
                for (child in snapshot.children) {
                    val keyword = child.getValue(String::class.java)
                    if (keyword != null) {
                        keywords.add(Keyword(keyword))  // 리스트에 키워드를 추가합니다.
                    }
                }
                liveData.postValue(keywords)  // LiveData에 데이터를 설정합니다.
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return liveData  // 데이터가 담긴 LiveData를 반환합니다.
    }
}
