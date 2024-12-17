package com.example.oopproject.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.oopproject.Adapter.CommentAdapter
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentContentBinding
import com.example.oopproject.viewModel.PostDetailViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentFragment : Fragment() {
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CommentAdapter
    private val viewModel: PostDetailViewModel by viewModels()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
        setupCommentSubmission(postId)
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

                // 신청 버튼 상태 설정
                button3.apply {
                    isEnabled = post.apply == "NONE"
                    text = if (post.apply == "APPLIED") "신청완료" else "신청"
                }

                post.imageUrl?.let { imageUrl ->
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.image_load)
                        .error(R.drawable.image_error)
                        .into(imageView5)
                } ?: run {
                    Log.d("ContentFragment", "No image URL found for post: ${post.postId}")
                    imageView5.setImageResource(R.drawable.profile)
                    imageView5.setOnClickListener {
                        Toast.makeText(requireContext(), "Failed to load image for post ${post.postId}", Toast.LENGTH_SHORT).show()
                    }
                }
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

        viewModel.commentStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                binding?.editTextText?.text?.clear()
                Toast.makeText(context, "댓글이 작성되었습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "댓글 작성에 실패했습니다", Toast.LENGTH_SHORT).show()
            }
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
            val postId = arguments?.getString("postId")
            val post = viewModel.postDetail.value
            if (postId != null && post != null) {
                if (post.apply == "NONE") {
                    viewModel.updateApplyStatus(postId, post.apply)
                    binding?.button3?.isEnabled = false  // 버튼 비활성화
                    Toast.makeText(context, "신청되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "이미 신청한 게시글입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 지도 버튼
        binding?.imageButton6?.setOnClickListener {
            findNavController().navigate(R.id.action_contentFragment_to_mapDialogFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCommentSubmission(postId: String?) {
        binding?.cmtMakeBnt?.setOnClickListener(CommentSubmitClickListener(postId))
    }

    // 댓글 제출을 위한 별도의 ClickListener 클래스
    private inner class CommentSubmitClickListener(private val postId: String?) : View.OnClickListener {
        override fun onClick(view: View) {
            val commentText = binding?.editTextText?.text?.toString()?.trim()

            if (commentText.isNullOrEmpty()) {
                Toast.makeText(context, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
                return
            }

            if (postId == null) {
                Toast.makeText(context, "게시글 정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                return
            }

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(context, "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
                return
            }

            // Firebase Realtime Database에서 사용자의 닉네임 가져오기
            Firebase.database.reference.child("users").child(currentUser.uid).child("nickname")
                .get()
                .addOnSuccessListener { snapshot ->
                    val nickname = snapshot.getValue(String::class.java) ?: "Anonymous"
                    // 여기서 ViewModel의 createComment 함수 호출
                    viewModel.createComment(postId, nickname, commentText)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "사용자 정보를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
