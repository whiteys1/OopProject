package com.example.oopproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.appliedPostAdapter
import com.example.oopproject.databinding.FragmentCalendarBinding
import com.example.oopproject.viewModel.PostsViewModel
import java.text.SimpleDateFormat
import java.util.Date

class CalendarFragment : Fragment() {

    private var binding: FragmentCalendarBinding? = null
    private val viewModel: PostsViewModel by activityViewModels()
    private val appliedPostAdapter by lazy { appliedPostAdapter(emptyList()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding?.recAppPost?.layoutManager = LinearLayoutManager(context)
        binding?.recAppPost?.adapter = appliedPostAdapter

        // 날짜 설정
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date(binding?.calendarView?.date ?: System.currentTimeMillis())
        binding?.dayText?.text = dateFormat.format(date)

        // CalendarView 날짜 변경 이벤트 // 첫번째 인자는 view <- 사용 X
        binding?.calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val day = "${year}/${month + 1}/${dayOfMonth}"
            binding?.dayText?.text = day
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filterByApply()

        // 필터링된 데이터 관찰 // 신청할 시에 변경되는 부분이라 observe 구현 추가
        viewModel.appliedPosts.observe(viewLifecycleOwner) { appliedPosts ->
            appliedPostAdapter.updatePosts(appliedPosts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // onDestroyView에서 binding을 null로 설정
    }
}
