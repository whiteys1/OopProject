package com.example.oopproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.CommentAdapter
import com.example.oopproject.databinding.FragmentContentBinding
import com.example.oopproject.viewModel.PostsViewModel
import com.example.oopproject.viewmodel.CommentViewModel

class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CommentAdapter
    private val postsViewModel: PostsViewModel by activityViewModels()
    private val commentViewModel: CommentViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        val postId = arguments?.getString("postId")
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
//        postsViewModel.selectedPosts.observe(viewLifecycleOwner){ post ->
//            if (post != null){
//                binding?.postTitle?.text = post.name
//                binding?.conKeyword1?.text = post.keyword.toString()
//            }
//        }
        setupRecyclerView()
        observeComments()
    }

    private fun setupRecyclerView() {
        adapter = CommentAdapter(emptyList())
        binding?.recView?.layoutManager = LinearLayoutManager(context)
        binding?.recView?.adapter = adapter
    }

    private fun observeComments() {

        // ViewModel에서 전달된 LiveData를 관찰
        commentViewModel.comments.observe(viewLifecycleOwner) { comments ->
            adapter.updateComments(comments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel.selectedPosts.observe(viewLifecycleOwner){ post ->
//            if (post != null){
//                binding?.postTitle?.text = post.name
//                binding?.conKeyword?.text = post.keyword.toString()
//            }
//        }
//        binding?.recView?.layoutManager = LinearLayoutManager(requireContext())
//        binding?.recView?.adapter = CommentAdapter(listOfComments)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}