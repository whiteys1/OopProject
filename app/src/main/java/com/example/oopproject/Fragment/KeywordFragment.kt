package com.example.oopproject.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
    private var _binding: FragmentKeywordBinding? = null
    private val binding get() = _binding
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
    }

    private fun setupRecyclerView() {
        adapter = KeywordAdapter()
        adapter.setOnKeywordSelectedListener(this)  // 리스너 설정
        binding?.rcKeyword?.layoutManager = GridLayoutManager(context,3) //Grid: N개씩 배열
        binding?.rcKeyword?.adapter = adapter
    }

    private fun observeKeywords() {
        // ViewModel에서 전달된 LiveData를 관찰하여 RecyclerView에 업데이트
        viewModel.keywords.observe(viewLifecycleOwner) { keywords ->
            adapter.setKeywords(keywords)  // 어댑터에 데이터 설정
        }
    }

    override fun onKeywordSelected(selectedKeywords: List<String?>) {
        binding?.apply {
            textView17.text = selectedKeywords[0] ?: "키워드 1"
            textView18.text = selectedKeywords[1] ?: "키워드 2"
            textView19.text = selectedKeywords[2] ?: "키워드 3"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupChipButtons() {
        binding?.chipGroup?.apply {
            // 모든 버튼에 대해 클릭 리스너 설정
            for (i in 0 until childCount) {
                val button = getChildAt(i) as? Button
                button?.setOnClickListener {
                    val newKeyword = button.text.toString()
                    if (!adapter.selectedKeywords.contains(newKeyword)) {
                        // 비어있는 첫 번째 위치 찾기
                        val emptyIndex = adapter.selectedKeywords.indexOfFirst { it == null }
                        if (emptyIndex != -1) {
                            // 비어있는 위치에 추가
                            adapter.selectedKeywords[emptyIndex] = newKeyword
                            adapter.currentIndex = (emptyIndex + 1) % 3
                        } else if (adapter.currentIndex < 3) {
                            // 모든 위치가 차있다면 순환
                            adapter.selectedKeywords[adapter.currentIndex] = newKeyword
                            adapter.currentIndex = (adapter.currentIndex + 1) % 3
                        }
                        adapter.notifyDataSetChanged()
                        onKeywordSelected(adapter.selectedKeywords)
                        Toast.makeText(context, "키워드가 추가되었습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "이미 선택된 키워드입니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupSearchButton() {
        binding?.button2?.setOnClickListener {
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

}
