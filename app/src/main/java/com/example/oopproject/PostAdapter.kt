package com.example.oopproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.oopproject.databinding.PostBinding

class PostAdapter (val posts : Array<Post>) : RecyclerView.Adapter<PostAdapter.Holder>() {

    //필터처리된 글 목록 리스트 선언 밑 필터 함수
    var filteredPosts:List<Post> = posts.toList()

    fun filterByKeyword(selectedKeyword: String) {
        filteredPosts = posts.filter { post ->
            post.keyword.contains(selectedKeyword)
        }
        notifyDataSetChanged() // 변경 사항을 RecyclerView에 실시간으로 반영하여 재바인딩되게끔 하는 역할
    }

    class Holder(val binding: PostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post:Post){

            binding.txtName.text = post.name
            binding.txtKeyarr.text = post.keyword.joinToString( ", " )
            binding.txtDesciption.text = post.description
            binding.txtDuedate.text = post.dueDate

            binding.LikeOrNot.setOnClickListener {
                post.like = when(post.like){
                    ELike.LIKE -> ELike.NONE
                    ELike.NONE -> ELike.LIKE
                }

                binding.LikeOrNot.setImageResource(
                    when (post.like) {
                        ELike.LIKE -> R.drawable.filledlike
                        ELike.NONE -> R.drawable.emptylike
                    }
                )
            }

            binding.txtContent.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_contentFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = PostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount() = filteredPosts.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(filteredPosts[position])
    }
}