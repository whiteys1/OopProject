package com.example.oopproject.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.oopproject.Post
import com.example.oopproject.R
import com.example.oopproject.databinding.AppliedpostBinding
import java.util.Date
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

val dateFormat = SimpleDateFormat("yyyy/MM/dd")

class appliedPostAdapter(private var posts: List<Post>, private val onDateClick: ((String) -> Unit)? = null) : RecyclerView.Adapter<appliedPostAdapter.Holder>() {
    class Holder(val binding: AppliedpostBinding, private val onDateClick: ((String) -> Unit)? = null) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            val targetDate = dateFormat.parse(post.date)
            val currentDate = Date()
            val Dday = TimeUnit.MILLISECONDS.toDays(targetDate.time - currentDate.time)

            binding.applyDday.text = "D-$Dday"
            binding.applyDate.text = post.date
            binding.applyName.text = post.name

            // 날짜 클릭 리스너 추가
            binding.applyDate.setOnClickListener {
                onDateClick?.invoke(post.date)      //날짜를 외부로 콜백을 통해 전달
            }

            binding.applyContent.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("postId", post.postId)
                }
                it.findNavController().navigate(R.id.action_calendarFragment_to_contentFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AppliedpostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onDateClick)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(posts[position])
    }

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}