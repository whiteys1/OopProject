package com.example.oopproject.Fragment

import android.graphics.Color
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
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.text.SimpleDateFormat
import java.util.Date


class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding
    private val viewModel: PostsViewModel by activityViewModels()
    private val appliedPostAdapter by lazy {
        appliedPostAdapter(emptyList()) { clickedDate ->    //빈 목록으로 우선 초기화
            val dateParts = clickedDate.split("/")
            val year = dateParts[0].toInt()
            val month = dateParts[1].toInt() - 1  // CalendarDay는 0부터 시작
            val day = dateParts[2].toInt()

            val calendarDay = CalendarDay.from(year, month, day)    //캘린더데이 객체 생성
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

        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date(System.currentTimeMillis())
        binding?.dayText?.text = dateFormat.format(date)

        val today = CalendarDay.from(date)          //현재 날짜를 캘린더데이 객체로 만들어준 후
        binding?.calendarView?.setDateSelected(today, true)     //화면에 해당부분 표시

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
            val eventDecorator = EventMarker(Color.MAGENTA, appliedDates)
            binding?.calendarView?.addDecorators(eventDecorator)    //점 표시
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
                    }
                }else{
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