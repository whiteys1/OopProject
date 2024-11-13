package com.example.oopproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oopproject.databinding.FragmentSearchBinding

class KeywordFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: KeywordAdapter
    private val viewModel: KeywordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeKeywords()
    }

    private fun setupRecyclerView() {
        adapter = KeywordAdapter()
        binding?.rcKeyword?.layoutManager = GridLayoutManager(context,3) //Grid: N개씩 배열
        binding?.rcKeyword?.adapter = adapter
    }

    private fun observeKeywords() {
        // ViewModel에서 전달된 LiveData를 관찰하여 RecyclerView에 업데이트
        viewModel.keywords.observe(viewLifecycleOwner) { keywords ->
            adapter.setKeywords(keywords)  // 어댑터에 데이터 설정
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
