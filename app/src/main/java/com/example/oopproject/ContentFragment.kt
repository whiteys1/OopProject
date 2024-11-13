package com.example.oopproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.databinding.FragmentContentBinding

class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding

    val listOfComments = arrayOf(
        Comment("User1", "Great post!", "10:30 AM"),
        Comment("User2", "Thanks for sharing.", "11:00 AM"),
        Comment("User3", "김치 맛있어요.", "11:00 AM"),
        Comment("User4", "코리아 화이팅.", "11:00 AM"),
        Comment("User5", "제로 사이다.", "11:00 AM"),
        Comment("User6", "대한 독립 만세.", "11:00 AM"),
        Comment("User7", "안녕하세요.", "11:00 AM"),
        Comment("User8", "Hello world.", "11:00 AM")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recView?.adapter = CommentsAdapter(listOfComments)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}