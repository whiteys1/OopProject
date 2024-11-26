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
                // 버튼 클릭시 로직 구현
                selectedKeywords[currentIndex] = keyword.keyword  // 배열의 현재 위치에 키워드를 추가
                currentIndex = (currentIndex + 1) % 3  // 인덱스를 순환
                listener?.onKeywordSelected(selectedKeywords)
                // 테스트 코드
                //Toast.makeText(itemView.context, "${keyword.keyword} clicked!", Toast.LENGTH_SHORT).show()
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