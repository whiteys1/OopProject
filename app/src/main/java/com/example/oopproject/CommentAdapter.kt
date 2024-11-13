package com.example.oopproject

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oopproject.databinding.ListCommentsBinding
import android.view.LayoutInflater


class CommentsAdapter(val comments:Array<Comment>):RecyclerView.Adapter<CommentsAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size

    class Holder(private val binding: ListCommentsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment){
            binding.cmt.text = comment.comment
            binding.UserProfile.setImageResource(R.drawable.profile)
            binding.userName.text = comment.nickname
            binding.date.text = comment.createdAt
        }
    }
}
