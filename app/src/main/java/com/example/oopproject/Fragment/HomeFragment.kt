package com.example.oopproject.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.PostAdapter
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentHomeBinding
import com.example.oopproject.viewModel.PostsViewModel
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: PostsViewModel by activityViewModels()
    private val postAdapter by lazy { PostAdapter(viewModel) }
    private var currentSelectedChip: Chip? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding?.recPost?.layoutManager = LinearLayoutManager(context)
        binding?.recPost?.adapter = postAdapter

        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            database.child("users").child(userId).get().addOnSuccessListener { datasnapshot ->
                val nickname = datasnapshot.child("nickname").getValue(String::class.java) ?: "User Error"

                binding?.userIcon?.setImageResource(R.drawable.profile)
                binding?.userName?.text = "$nickname"
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChipClickListeners()

        // 필터링된 데이터 관찰
        viewModel.filteredPosts.observe(viewLifecycleOwner) { filteredPosts ->
            postAdapter.submitList(filteredPosts)
        }

        // 전체 posts 관찰 (초기)
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            if (viewModel.filteredPosts.value.isNullOrEmpty()) {
                postAdapter.submitList(posts)
            }
        }
    }

    // 키워드를 기준으로 필터링 요청
    private fun filterAndAlert(selectedKeyword: String) {
        viewModel.filterByKeyword(selectedKeyword)
        if (viewModel.filteredPosts.value.isNullOrEmpty()) {
            binding?.txtRecom?.text = "추천 컨텐츠가 없습니다."
            Toast.makeText(context, "필터링된 글 목록이 없습니다.", Toast.LENGTH_SHORT).show()
        } else {
            binding?.txtRecom?.text = "추천 컨텐츠"
        }
    }

    // 칩 클릭 리스너 설정
    private fun setChipClickListeners() {
        val chipClickListener = { chip: Chip ->
            // 현재 클릭된 칩이 이전에 선택한 칩과 같은 경우
            if (chip == currentSelectedChip) {
                chip.setTextColor(Color.BLACK)
                viewModel.clearFilter()
                currentSelectedChip = null
                binding?.txtRecom?.text = "추천 컨텐츠"
            } else {
                currentSelectedChip?.setTextColor(Color.BLACK)

                chip.setTextColor(Color.RED)

                val selectedKeyword = chip.text.toString()
                filterAndAlert(selectedKeyword)
                currentSelectedChip = chip
            }
        }

        binding?.apply {
            listOf(chipEmploy, chipClub, chipOn, chipOff, chipHob, chipOut, chipCon, chipExp).forEach { chip ->
                chip.setOnClickListener { chipClickListener(it as Chip) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}