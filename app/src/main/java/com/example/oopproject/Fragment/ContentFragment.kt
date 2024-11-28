package com.example.oopproject.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.CommentAdapter
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentContentBinding
import com.example.oopproject.viewModel.PostDetailViewModel
import com.example.oopproject.viewModel.PostsViewModel

class ContentFragment : Fragment() {
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CommentAdapter
    private val viewModel: PostDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = arguments?.getString("postId")
        postId?.let {
            viewModel.initialize(it)
        }

        setupRecyclerView()
        observeData()
        setupButtons()
    }

    private fun setupRecyclerView() {
        adapter = CommentAdapter(emptyList())
        binding?.recView?.layoutManager = LinearLayoutManager(context)
        binding?.recView?.adapter = adapter
    }

    private fun observeData() {
        viewModel.postDetail.observe(viewLifecycleOwner) { post ->
            binding?.apply {
                postTitle.text = post.name
                textView2.text = post.description.replace("\\n", "\n")  // 컨텐츠 세부사항
                conKeyword1.text = post.keyword.getOrNull(0) ?: ""
                conKeyword2.text = post.keyword.getOrNull(1) ?: ""
                conKeyword3.text = post.keyword.getOrNull(2) ?: ""
                textView.text = post.dueDate      // 마감일자
                textView22.text = "작성자 이름"    // 사용자명 표시
            }
        }

        viewModel.likeStatus.observe(viewLifecycleOwner) { likeStatus ->
            binding?.imageButton3?.setImageResource(
                when (likeStatus) {
                    "LIKE" -> R.drawable.filledlike
                    else -> R.drawable.emptylike
                }
            )
        }

        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            adapter.updateComments(comments)
        }
    }

    private fun setupButtons() {
        // 뒤로가기 버튼
        binding?.backImageButton?.setOnClickListener {
            findNavController().navigateUp()
        }

        // 좋아요 버튼
        binding?.imageButton3?.setOnClickListener {
            val postId = arguments?.getString("postId")
            val currentStatus = viewModel.likeStatus.value
            if (postId != null && currentStatus != null) {
                viewModel.updateLikeStatus(postId, currentStatus)
            }
        }

        // 신청 버튼
        binding?.button3?.setOnClickListener {
            //신청 구현
        }

        // 지도 버튼
        binding?.imageButton6?.setOnClickListener {
            // 지도 기능 구현
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
