package com.example.oopproject.Adqpter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.oopproject.Keyword
import com.example.oopproject.databinding.ListKeywordBinding

class KeywordAdapter : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {
    private var keywords: List<Keyword> = listOf()
    var selectedKeywords = arrayOfNulls<String>(3)
    private var currentIndex = 0

    interface OnKeywordSelectedListener {
        fun onKeywordSelected(selectedKeywords: Array<String?>)
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

                    // 테스트용 토스트 메시지 (선택사항)
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

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(keywords[position])
    }

    override fun getItemCount() = keywords.size

    fun setKeywords(keywords: List<Keyword>) {
        this.keywords = keywords
        notifyDataSetChanged() //키워드의 데이터베이스가 변화되면 알려줌
    }


}