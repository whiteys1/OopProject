package com.example.oopproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.PostAdapter
import com.example.oopproject.EGen
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentHomeBinding
import com.example.oopproject.viewModel.PostsViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest

class User(val userName:String, val gen: EGen)

class HomeFragment : Fragment() {

    private var user = User("KAU", EGen.COMPANY)
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: PostsViewModel by activityViewModels()
    private val postAdapter by lazy { PostAdapter(emptyList(), viewModel) }
    private var currentSelectedChip: Chip? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding?.recPost?.layoutManager = LinearLayoutManager(context)
        binding?.recPost?.adapter = postAdapter

        setChipClickListeners()

        binding?.userIcon?.setImageResource(R.drawable.profile)

        binding?.userName?.text = user.userName

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화: 전체 게시물을 어댑터에 전달하여 모든 글 표시
        viewModel.posts.observe(viewLifecycleOwner){ posts ->
            postAdapter.updatePosts(posts)
            viewModel.clearFilter()
        }

        // 필터링된 데이터 관찰
        viewModel.filteredPosts.observe(viewLifecycleOwner) { filteredPosts ->
            postAdapter.updatePosts(filteredPosts)
        }
    }

    // 키워드를 기준으로 필터링 요청
    private fun filterAndAlert(selectedKeyword: String) {
        viewModel.filterByKeyword(selectedKeyword)
        if (viewModel.filteredPosts.value.isNullOrEmpty()) {
            Toast.makeText(context, "필터링된 글 목록이 없습니다.", Toast.LENGTH_SHORT).show()
            binding?.txtRecom?.text = "추천 컨텐츠가 없습니다."
        } else {
            binding?.txtRecom?.text = "추천 컨텐츠"
        }
    }

    // 칩 클릭 리스너 설정
    private fun setChipClickListeners() {
        val chipClickListener = { chip: Chip ->
            // 현재 클릭된 칩이 이전에 선택한 칩과 같은 경우
            if (chip == currentSelectedChip) {
                // 필터 해제 (모든 게시물 다시 로드)
                viewModel.clearFilter()
                currentSelectedChip = null
                postAdapter.updatePosts(viewModel.posts.value ?: emptyList())
                binding?.txtRecom?.text = "모든 컨텐츠"
            } else {
                // 새로운 칩을 선택한 경우
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



// 페이징 시도
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.postsFlow.collectLatest { pagingData ->
//                postAdapter.submitData(pagingData)
//            }
//        }
//
//        // 필터링된 데이터 관찰
//        viewModel.filteredPosts.observe(viewLifecycleOwner) { filteredPosts ->
//            val pagingData = PagingData.from(filteredPosts)
//            lifecycleScope.launchWhenStarted {
//                postAdapter.submitData(pagingData)
//            }
//        }
//    }

//        // 초기화: 전체 게시물을 어댑터에 전달하여 모든 글 표시
//        viewModel.posts.observe(viewLifecycleOwner){ posts ->
//            postAdapter.submit(posts)
//            viewModel.clearFilter()
//        }