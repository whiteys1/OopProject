package com.example.oopproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView

class KeywordAdapter : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {
    private var keywords: List<Keyword> = listOf()

    class KeywordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val button: Button = itemView.findViewById(R.id.keywordButton)

        fun bind(keyword: Keyword) {
            button.text = keyword.keyword
            button.setOnClickListener {
                // 버튼 클릭시 로직 구현
                // 테스트 코드
                // Toast.makeText(itemView.context, "${keyword.keyword} clicked!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_keyword, parent, false)
        return KeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(keywords[position])
    }

    override fun getItemCount() = keywords.size

    fun setKeywords(keywords: List<Keyword>) {
        this.keywords = keywords
        notifyDataSetChanged()
    }


}