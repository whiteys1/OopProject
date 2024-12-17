package com.example.oopproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.oopproject.Keyword
import com.example.oopproject.databinding.ListKeywordBinding

class KeywordAdapter : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {
    private var keywords: List<Keyword> = listOf()
    private var filteredKeywords: List<Keyword> = listOf()  // 필터링된 키워드 리스트
    var selectedKeywords : MutableList<String?> = mutableListOf(null, null, null) // 값이 변경이 되어야 하므로 var로 선언
    private var currentIndex = 0 //키워드 프래그먼트에서 접근을 위해 internal로 선언, 값이 변경이 되어야 하므로 var로 선언

    interface OnKeywordSelectedListener {
        fun onKeywordSelected(selectedKeywords: List<String?>)
    }

    private var listener: OnKeywordSelectedListener? = null

    fun setOnKeywordSelectedListener(listener: OnKeywordSelectedListener) {
        this.listener = listener
    }

    // selectedKeywords에 키워드 추가 로직
    fun addKeyword(newKeyword: String): Boolean {
        // 이미 선택된 키워드인지 확인
        if (selectedKeywords.contains(newKeyword)) {
            return false
        }
        // currentIndex 위치에 키워드 추가 (3개를 초과하면 순환)
        selectedKeywords[currentIndex % 3] = newKeyword
        currentIndex = (currentIndex + 1) % 3
        return true
    }

    // keword 선택 클래스
    inner class KeywordViewHolder(private val binding: ListKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword) { // 키워드 데이터를 뷰에 바인딩하는 함수
            binding.keywordButton.text = keyword.keyword
            binding.keywordButton.setOnClickListener {
                if (addKeyword(keyword.keyword)) {
                    listener?.onKeywordSelected(selectedKeywords)
                    Toast.makeText(itemView.context, "키워드가 추가되었습니다", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(itemView.context, "이미 선택된 키워드입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val binding = ListKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordViewHolder(binding)
    }

    // 키워드 리스트 설정
    fun setKeywords(keywords: List<Keyword>) {
        this.keywords = keywords
        this.filteredKeywords = keywords  // 초기에는 모든 키워드 표시
        notifyDataSetChanged()
    }

    // 검색어 필터링
    fun filter(query: String) {
        filteredKeywords = if (query.isEmpty()) {
            keywords  // 검색어가 없으면 모든 키워드 표시
        } else {
            keywords.filter { keyword ->
                keyword.keyword.contains(query, ignoreCase = true)  // 대소문자 구분 없이 검색
            }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(filteredKeywords[position])  // filteredKeywords 사용
    }

    override fun getItemCount() = filteredKeywords.size  // filteredKeywords 크기 반환
}