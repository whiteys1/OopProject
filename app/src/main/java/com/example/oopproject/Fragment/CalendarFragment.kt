package com.example.oopproject.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.appliedPostAdapter
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentCalendarBinding
import com.example.oopproject.viewModel.PostsViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.text.SimpleDateFormat
import java.util.Date


class CalendarFragment : Fragment(){
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding
    private val viewModel: PostsViewModel by activityViewModels()
    private val appliedPostAdapter by lazy { appliedPostAdapter(emptyList()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding?.recAppPost?.layoutManager = LinearLayoutManager(context)
        binding?.recAppPost?.adapter = appliedPostAdapter

        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date(System.currentTimeMillis())
        binding?.dayText?.text = dateFormat.format(date)

        // 현재 날짜를 CalendarDay 객체로 변환
        val today = CalendarDay.from(date)

        // 현재 날짜를 달력에서 선택된 상태로 설정
        binding?.calendarView?.setDateSelected(today, true)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.filterByApply()

        viewModel.appliedPosts.observe(viewLifecycleOwner) { appliedPosts ->
            appliedPostAdapter.updatePosts(appliedPosts)
            val appliedDates = HashSet<CalendarDay>()
            appliedPosts.forEach { post ->
                val dateParts = post.date.split("/")
                val year = dateParts[0].toInt()
                val month = dateParts[1].toInt() - 1 // CalendarDay에서 월은 0부터 시작
                val day = dateParts[2].toInt()
                appliedDates.add(CalendarDay.from(year, month, day))
            }
            val eventDecorator = EventMarker(Color.RED, appliedDates)
            binding?.calendarView?.addDecorators(eventDecorator)
        }

        //매개변수 (1: 객체 자체, 2: day객체, 3: 선택상태)
        binding?.calendarView?.setOnDateChangedListener { _, date, _ ->
            val selectedDate = "${date.year}/${date.month + 1}/${date.day}"
            binding?.dayText?.text = selectedDate
            viewModel.filterInCalendar(selectedDate)

            viewModel.selectedPosts.observe(viewLifecycleOwner) { selectedPosts ->
                if (selectedPosts.isNullOrEmpty()){
                    viewModel.appliedPosts.value?.let{
                        appliedPostAdapter.updatePosts(it)
                        binding?.applyList?.text = "전체 신청목록"
                    }
                }else{
                    binding?.applyList?.text = "${date.month + 1}/${date.day} 신청목록"
                    appliedPostAdapter.updatePosts(selectedPosts)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // onDestroyView에서 binding을 null로 설정
    }
}

class EventMarker(private val color:Int, private val dates: HashSet<CalendarDay>):DayViewDecorator{
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(10f, color))
    }
}