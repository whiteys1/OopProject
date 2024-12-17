package com.example.oopproject.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oopproject.Adapter.AppliedPostAdapter
import com.example.oopproject.Adapter.dateFormat
import com.example.oopproject.databinding.FragmentCalendarBinding
import com.example.oopproject.viewModel.PostsViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding

    private val viewModel: PostsViewModel by activityViewModels()

    private val appliedPostAdapter by lazy {
        AppliedPostAdapter { clickedDate ->
            val calendarDay = clickedDate.toCalendarDay()
            selectCalendarDate(calendarDay)

            binding?.dayText?.text = clickedDate
            viewModel.filterInCalendar(clickedDate)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding?.recAppPost?.layoutManager = LinearLayoutManager(context)
        binding?.recAppPost?.adapter = appliedPostAdapter

        setToday()
        val today = binding?.dayText?.text.toString().toCalendarDay()
        val todayDecorator = EventMarker(Color.BLUE, hashSetOf(today))
        binding?.calendarView?.addDecorators(todayDecorator)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.filterByApply()                                                   //신청 글 목록 생성

        viewModel.appliedPosts.observe(viewLifecycleOwner) { appliedPosts ->        //신청 글 목록
            appliedPostAdapter.submitList(appliedPosts)

            val appliedDates = appliedPosts.map { it.date.toCalendarDay() }.toHashSet()
            val eventDecorator = EventMarker(Color.MAGENTA, appliedDates)
            binding?.calendarView?.addDecorators(eventDecorator)
        }

        viewModel.selectedPosts.observe(viewLifecycleOwner) { selectedPosts ->      //달력에서 선택된 날짜의 글 목록
            if (selectedPosts.isNullOrEmpty()){
                viewModel.appliedPosts.value?.let{
                    appliedPostAdapter.submitList(it)
                }
            }else{
                appliedPostAdapter.submitList(selectedPosts)
            }
        }

        //매개변수 (1: 객체 자체, 2: day객체, 3: 선택상태)
        binding?.calendarView?.setOnDateChangedListener { _, date, _ ->             //달력 날짜 선택
            val selectedDate = dateFormat.format(Date(date.year - 1900, date.month, date.day))
            binding?.dayText?.text = selectedDate
            viewModel.filterInCalendar(selectedDate)
        }
    }

    override fun onResume() {           //다른 화면에 갔다 올 때 오늘 날짜로 초기화
        super.onResume()
        setToday()
    }

    private fun selectCalendarDate(date: CalendarDay) {             //선택한 날짜 선택 표시
        binding?.calendarView?.let { calendar ->
            calendar.clearSelection()
            calendar.currentDate = date
            calendar.setDateSelected(date, true)
        }
    }

    private fun setToday() {
        val today = dateFormat.format(Date(System.currentTimeMillis()))
        val calendarDay = today.toCalendarDay()

        selectCalendarDate(calendarDay)
        binding?.dayText?.text = today
        viewModel.filterInCalendar(today)
    }

    private fun String.toCalendarDay():CalendarDay{                 //String -> 캘린더데이 객체로 변환
        val dateParts = this.split("/")
        return CalendarDay.from(dateParts[0].toInt(), dateParts[1].toInt() - 1, dateParts[2].toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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