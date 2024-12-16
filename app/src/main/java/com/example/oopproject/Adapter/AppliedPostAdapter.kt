package com.example.oopproject.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.oopproject.Post
import com.example.oopproject.R
import com.example.oopproject.databinding.AppliedpostBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

val dateFormat = SimpleDateFormat("yyyy/MM/dd")

class AppliedPostAdapter(private val onDateClick: ((String) -> Unit)? = null) : ListAdapter<Post, AppliedPostAdapter.Holder>(AppliedPostDiffCallback()) {
    class Holder(val binding: AppliedpostBinding, private val onDateClick: ((String) -> Unit)? = null) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            val targetDate = dateFormat.parse(post.date)
            val currentDate = Date()
            val Dday = TimeUnit.MILLISECONDS.toDays((targetDate.time) - currentDate.time)

            // D-Day 설정
            binding.applyDday.text = when(Dday.toInt()) {
                0 -> "Dday"
                else -> "D-$Dday"
            }

            binding.applyDate.text = post.date
            binding.applyName.text = post.name

            // 날짜 클릭 리스너 추가
            binding.applyDate.setOnClickListener {
                onDateClick?.invoke(post.date) // 외부 콜백으로 전달
            }

            binding.applyContent.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("postId", post.postId) // postId를 전달
                }
                it.findNavController().navigate(R.id.action_calendarFragment_to_contentFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AppliedpostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onDateClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position)) // getItem() 사용
    }
}

private class AppliedPostDiffCallback : DiffUtil.ItemCallback<Post>() {        //post들을 비교

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.postId == newItem.postId         //두 객체가 같은 것인지
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem                       //내용이 변경되었는지
    }
}
