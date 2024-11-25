package com.example.oopproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adqpter.CommentAdapter
import com.example.oopproject.viewmodel.CommentViewModel
import com.example.oopproject.databinding.FragmentContentBinding

class ContentFragment : Fragment() {
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CommentAdapter
    private val viewModel: CommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.initialize(postId)

        // ViewModel에서 전달된 LiveData를 관찰
        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            adapter.updateComments(comments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}