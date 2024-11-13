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
    private val database = Firebase.database  // Firebase 데이터베이스 인스턴스를 가져옵니다.
    private val keywordsRef = database.getReference()  // "keywords" 레퍼런스를 가져옵니다.

    fun fetchKeywords(): LiveData<List<Keyword>> {
        val liveData = MutableLiveData<List<Keyword>>()  // 데이터를 담을 LiveData를 생성합니다.

        // 데이터베이스에서 데이터 변경을 리스닝합니다.
        keywordsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val keywords = mutableListOf<Keyword>()  // 데이터를 저장할 리스트를 생성합니다.
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
