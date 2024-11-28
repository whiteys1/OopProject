package com.example.oopproject.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oopproject.Adapter.KeywordAdapter
import com.example.oopproject.databinding.FragmentKeywordBinding
import com.example.oopproject.viewModel.KeywordViewModel

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
        observeKeywords()
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

    override fun onKeywordSelected(selectedKeywords: Array<String?>) {
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
}
