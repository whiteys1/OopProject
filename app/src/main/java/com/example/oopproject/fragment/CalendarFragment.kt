package com.example.oopproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentCalendarBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class CalendarFragment : Fragment() {

    private var binding:FragmentCalendarBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 객체 생성
        val dayText: TextView = view.findViewById(R.id.dayText)
        val calendarView: CalendarView = view.findViewById(R.id.calendarView)

        // 날짜 형태
        val dateFormat:DateFormat = SimpleDateFormat("yyyy/MM/dd")

        // date타입
        val date:Date = Date(calendarView.date)

        // 현재 날짜 담기
        dayText.text = dateFormat.format(date)

        // CalendarView 날짜 변경 이벤트
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            // 날짜 변수에 담기
            var day: String = "${year}/${month + 1}/${dayOfMonth}"
            // 변수 텍스트뷰에 담기
            dayText.text = day
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}