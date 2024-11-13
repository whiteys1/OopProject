package com.example.oopproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip

class User(val userName:String, val gen: EGen)

class HomeFragment : Fragment() {

    private var user = User("KAU", EGen.COMPANY)
    private var binding: FragmentHomeBinding? = null

    val posts = arrayOf(
        Post("취업데이", arrayOf("취업", "오프라인"), "2024-12-31", "2025-01-01", EStatus.APPLIED, ELike.NONE, "취업 특강"),
        Post("기업특강", arrayOf("취업", "온라인"), "2024-12-31", "2025-01-25", EStatus.APPLIED, ELike.LIKE, "삼성 특강"),
        Post("야구데이", arrayOf("취미", "온라인"), "2024-12-31", "2025-01-31", EStatus.NONE, ELike.LIKE, "취미(야구) 행사"),
        Post("동아리데이", arrayOf("동아리", "오프라인"), "2024-11-19", "2024-12-31", EStatus.NONE, ELike.NONE, "동아리 행사")
    )

    private val postAdapter by lazy { PostAdapter(posts) }

    // 선택된 키워드에 따른 필터링 기능
    private fun filterByKeyword(selectedKeyword: String) {
        postAdapter.filterByKeyword(selectedKeyword)
        if (postAdapter.filteredPosts.isEmpty()) {
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
            filterByKeyword(selectedKeyword)
        }

        binding?.apply {
            listOf(chipEmploy, chipClub, chipOn, chipOff, chipHob, chipOut, chipCon, chipExp).forEach { chip ->
                chip.setOnClickListener { chipClickListener(it as Chip) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding?.recPost?.layoutManager = LinearLayoutManager(context)
        binding?.recPost?.adapter = postAdapter

        setChipClickListeners()

        binding?.userIcon?.setImageResource(when(user.gen){
            EGen.MALE -> R.drawable.maleicon
            EGen.FEMALE -> R.drawable.femaleicon
            EGen.COMPANY -> R.drawable.companyicon
        })

        binding?.userName?.text = user.userName

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*setChipClickListeners()
        binding?.imgbnt.setOnClickListener{

        }*/


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}