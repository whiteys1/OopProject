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
    var selectedKeywords : MutableList<String?> = mutableListOf(null, null, null)
    internal var currentIndex = 0 //키워드 프래그먼트에서 접근을 위해 internal로 선언

    interface OnKeywordSelectedListener {
        fun onKeywordSelected(selectedKeywords: List<String?>)
    }

    private var listener: OnKeywordSelectedListener? = null

    fun setOnKeywordSelectedListener(listener: OnKeywordSelectedListener) {
        this.listener = listener
    }

    //외부값을 받기 위해 inner class로 선언
    inner class KeywordViewHolder(private val binding: ListKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword) {
            binding.keywordButton.text = keyword.keyword
            binding.keywordButton.setOnClickListener {
                val newKeyword = keyword.keyword
                // 중복 체크
                if (!selectedKeywords.contains(newKeyword)) {
                    // 비어있는 첫 번째 위치 찾기
                    val emptyIndex = selectedKeywords.indexOfFirst { it == null }
                    if (emptyIndex != -1) {
                        // 비어있는 위치에 추가
                        selectedKeywords[emptyIndex] = newKeyword
                        currentIndex = (emptyIndex + 1) % 3
                    } else if (currentIndex < 3) {
                        // 모든 위치가 차있다면 순환
                        selectedKeywords[currentIndex] = newKeyword
                        currentIndex = (currentIndex + 1) % 3
                    }
                    listener?.onKeywordSelected(selectedKeywords)

                    Toast.makeText(itemView.context, "키워드가 추가되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    // 이미 선택된 키워드라면 토스트 메시지 표시
                    Toast.makeText(itemView.context, "이미 선택된 키워드입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val binding = ListKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordViewHolder(binding)
    }

    fun setKeywords(keywords: List<Keyword>) {
        this.keywords = keywords
        this.filteredKeywords = keywords  // 초기에는 모든 키워드 표시
        notifyDataSetChanged()
    }

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