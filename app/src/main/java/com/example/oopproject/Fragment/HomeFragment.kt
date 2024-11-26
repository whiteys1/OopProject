package com.example.oopproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.PostAdapter
import com.example.oopproject.EGen
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentHomeBinding
import com.example.oopproject.viewModel.PostsViewModel
import com.google.android.material.chip.Chip

class User(val userName:String, val gen: EGen)

class HomeFragment : Fragment() {

    private var user = User("KAU", EGen.COMPANY)
    private var binding: FragmentHomeBinding? = null
    private val viewModel: PostsViewModel by activityViewModels()
    private val postAdapter by lazy { PostAdapter(emptyList(), viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding?.recPost?.layoutManager = LinearLayoutManager(context)
        binding?.recPost?.adapter = postAdapter

        setChipClickListeners()

        binding?.userIcon?.setImageResource(
            when (user.gen) {
                EGen.MALE -> R.drawable.maleicon
                EGen.FEMALE -> R.drawable.femaleicon
                EGen.COMPANY -> R.drawable.companyicon
            }
        )
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
            val selectedKeyword = chip.text.toString()
            filterAndAlert(selectedKeyword)
        }

        binding?.apply {
            listOf(chipEmploy, chipClub, chipOn, chipOff, chipHob, chipOut, chipCon, chipExp).forEach { chip ->
                chip.setOnClickListener { chipClickListener(it as Chip) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
