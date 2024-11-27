package com.example.oopproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adqpter.CommentAdapter
import com.example.oopproject.viewmodel.CommentViewModel
import com.example.oopproject.databinding.FragmentContentBinding
import com.example.oopproject.viewModel.PostsViewModel

class ContentFragment : Fragment() {
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CommentAdapter
    private val commentViewModel: CommentViewModel by viewModels()
    private val postsViewModel: PostsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)

        val postId = arguments?.getString("postId")
        Log.d("ContentFragment", "Received postId: $postId")

        if (postId != null){
            postsViewModel.findById(postId)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postsViewModel.selectedPosts.observe(viewLifecycleOwner){ post ->
            if (post != null){
                binding?.postTitle?.text = post.name
                binding?.conKeyword1?.text = post.keyword[0]
                binding?.conKeyword2?.text = post.keyword[1]
                binding?.conKeyword3?.text = ""
            }
        }
        setupRecyclerView()
        observeComments()
    }

    private fun setupRecyclerView() {
        adapter = CommentAdapter(emptyList())
        binding?.recView?.layoutManager = LinearLayoutManager(context)
        binding?.recView?.adapter = adapter
    }

    private fun observeComments() {
        // postId 가져오기
        val postId = arguments?.getString("postId") ?: "defaultPostId"
        commentViewModel.initialize(postId)

        // ViewModel에서 전달된 LiveData를 관찰
        commentViewModel.comments.observe(viewLifecycleOwner) { comments ->
            adapter.updateComments(comments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}