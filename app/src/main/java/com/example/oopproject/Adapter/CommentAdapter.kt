package com.example.oopproject.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oopproject.databinding.ListCommentsBinding
import android.view.LayoutInflater
import com.example.oopproject.Comment
import com.example.oopproject.R


class CommentAdapter(private var comments: List<Comment> = emptyList()) :
    RecyclerView.Adapter<CommentAdapter.Holder>() {

    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListCommentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size

    class Holder(private val binding: ListCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.apply {
                userName.text = comment.nickname
                cmt.text = comment.comment
                date.text = comment.createdAt
                userProfile.setImageResource(R.drawable.profile)
            }
        }
    }
}