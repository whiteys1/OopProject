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
            binding?.calendarView?.let { calendar ->
                calendar.currentDate = calendarDay  // 클릭된 달력 날짜로 이동
                calendar.clearSelection()  // 기존 선택 제거
                calendar.setDateSelected(calendarDay, true)  // 새로운 날짜 선택상태로 변경
            }

            binding?.dayText?.text = clickedDate
            viewModel.filterInCalendar(clickedDate)     //특정 날짜에 대한 일정만 화면에 표시
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding?.recAppPost?.layoutManager = LinearLayoutManager(context)
        binding?.recAppPost?.adapter = appliedPostAdapter

        val dateFormat = SimpleDateFormat("yyyy/MM/dd").apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")  // 한국 시간대로 명시적 설정
        }

        val date = Date(System.currentTimeMillis())
        binding?.dayText?.text = dateFormat.format(date)

        val today = dateFormat.format(date).toCalendarDay()         //현재 날짜를 캘린더데이 객체로 만들어준 후
        binding?.calendarView?.setDateSelected(today, true)     //화면에 해당부분 표시

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.filterByApply()

        viewModel.appliedPosts.observe(viewLifecycleOwner) { appliedPosts ->
            appliedPostAdapter.submitList(appliedPosts)
            val appliedDates = appliedPosts.map { it.date.toCalendarDay() }.toHashSet()

            val eventDecorator = EventMarker(Color.MAGENTA, appliedDates)
            binding?.calendarView?.addDecorators(eventDecorator)    //점 표시
        }

        viewModel.selectedPosts.observe(viewLifecycleOwner) { selectedPosts ->
            if (selectedPosts.isNullOrEmpty()){
                viewModel.appliedPosts.value?.let{
                    appliedPostAdapter.submitList(it)
                }
            }else{
                appliedPostAdapter.submitList(selectedPosts)
            }
        }

        //매개변수 (1: 객체 자체, 2: day객체, 3: 선택상태)
        binding?.calendarView?.setOnDateChangedListener { _, date, _ ->
            val selectedDate = "${date.year}/${date.month + 1}/${date.day}"
            binding?.dayText?.text = selectedDate
            viewModel.filterInCalendar(selectedDate)
        }
    }

    override fun onResume() {
        super.onResume()

        val dateFormat = SimpleDateFormat("yyyy/MM/dd").apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }
        val date = Date(System.currentTimeMillis())
        val today = dateFormat.format(date)

        binding?.calendarView?.let { calendar ->
            calendar.clearSelection()
            calendar.currentDate = today.toCalendarDay()
            calendar.setDateSelected(today.toCalendarDay(), true)
        }

        binding?.dayText?.text = today
        viewModel.filterInCalendar(today)
    }

    private fun String.toCalendarDay():CalendarDay{
        val dateParts = this.split("/")
        return CalendarDay.from(dateParts[0].toInt(), dateParts[1].toInt() - 1, dateParts[2].toInt())
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