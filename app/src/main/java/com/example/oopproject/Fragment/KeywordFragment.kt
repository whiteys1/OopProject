package com.example.oopproject.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oopproject.Adapter.KeywordAdapter
import com.example.oopproject.MainActivity
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentKeywordBinding
import com.example.oopproject.viewModel.KeywordViewModel
import com.example.oopproject.viewModel.PostsViewModel

class KeywordFragment : Fragment(), KeywordAdapter.OnKeywordSelectedListener  {
    private var _binding: FragmentKeywordBinding? = null    //메모리 누수 방지
    private val binding get() = _binding                    // 읽기 전용 프로퍼티
    private lateinit var adapter: KeywordAdapter
    private val viewModel: KeywordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKeywordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupChipButtons()
        observeKeywords()
        setupSearchButton()
        setupSearchView()
    }

    //리사이클러 뷰 설정 함수
    private fun setupRecyclerView() {
        adapter = KeywordAdapter()
        adapter.setOnKeywordSelectedListener(this)  // 리스너 설정
        binding?.rcKeyword?.layoutManager = GridLayoutManager(context,4) //Grid: N개씩 배열
        binding?.rcKeyword?.adapter = adapter
    }

    // 키워드 데이터 관찰 함수
    private fun observeKeywords() {
        // ViewModel에서 전달된 LiveData를 관찰하여 RecyclerView에 업데이트
        viewModel.keywords.observe(viewLifecycleOwner) { keywords ->
            adapter.setKeywords(keywords)  // 어댑터에 데이터 설정
        }
    }

    // 키워드 선택 시 호출되는 함수, 어뎁터에서 정의
    override fun onKeywordSelected(selectedKeywords: List<String?>) {
        binding?.apply {
            kwTxt1.text = selectedKeywords[0] ?: "키워드 1"
            kwTxt2.text = selectedKeywords[1] ?: "키워드 2"
            kwTxt3.text = selectedKeywords[2] ?: "키워드 3"
        }
    }

    // 메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 칩 내의 버튼 설정 함수 버튼을 누를 시 selectedKeywords에 추가
    private fun setupChipButtons() {
        binding?.chipGroup?.apply {
            for (i in 0 until childCount) {
                val button = getChildAt(i) as? Button
                button?.setOnClickListener {
                    val newKeyword = button.text.toString()
                    // 어뎁터의 addKeyword 함수 사용
                    if (adapter.addKeyword(newKeyword)) {
                        onKeywordSelected(adapter.selectedKeywords)
                        Toast.makeText(context, "키워드가 추가되었습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "이미 선택된 키워드입니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // 키워드 검색 버튼 설정 함수
    private fun setupSearchButton() {
        binding?.kwSearchBnt?.setOnClickListener {
            val selectedKeywords = adapter.selectedKeywords.filterNotNull()

            when {
                selectedKeywords.isEmpty() -> {
                    Toast.makeText(context, "키워드를 선택해주세요", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val viewModel: PostsViewModel by activityViewModels()
                    // 우선 필터링 적용
                    viewModel.filterByKeyword(selectedKeywords)
                    // 바로 화면 전환
                    (activity as? MainActivity)?.binding?.bottomNav?.selectedItemId = R.id.homeFragment
                }
            }
        }
    }

    // 검색창 설정 함수
    private fun setupSearchView() {
        binding?.srchKw?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })
    }
}
