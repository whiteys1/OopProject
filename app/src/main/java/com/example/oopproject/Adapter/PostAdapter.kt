package com.example.oopproject.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oopproject.Post
import com.example.oopproject.R
import com.example.oopproject.databinding.PostBinding
import com.example.oopproject.viewModel.PostsViewModel

class PostAdapter(private var posts: List<Post>, private val viewModel: PostsViewModel) : RecyclerView.Adapter<PostAdapter.Holder>() {
    class Holder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post, viewModel: PostsViewModel) {
            binding.LikeOrNot.setImageResource(
                when (post.like) {
                    "LIKE" -> R.drawable.filledlike
                    "NONE" -> R.drawable.emptylike
                    else -> R.drawable.emptylike
                })

            binding.txtName.text = post.name
            binding.txtKeyarr.text = post.keyword.joinToString(" | ")
            binding.txtDate.text = post.date

            post.imageUrl?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image_load)
                    .error(R.drawable.image_error)
                    .into(binding.contentImage)
            } ?: run {
                binding.contentImage.setImageResource(R.drawable.profile)
            }

            binding.LikeOrNot.setOnClickListener {
                viewModel.modifyLike(post)

                // **좋아요 상태 즉시 반영**
                post.like = if (post.like == "LIKE") "NONE" else "LIKE"
                binding.LikeOrNot.setImageResource(
                    when (post.like) {
                        "LIKE" -> R.drawable.filledlike
                        "NONE" -> R.drawable.emptylike
                        else -> R.drawable.emptylike
                    })
            }

            listOf(binding.contentImage, binding.txtName).forEach { toClick ->
                toClick.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("postId", post.postId)
                    }
                    it.findNavController().navigate(R.id.action_homeFragment_to_contentFragment, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = PostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(posts[position], viewModel)
    }

    // 외부에서 필터링된 데이터를 설정하고 갱신하기 위한 메서드
    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}
