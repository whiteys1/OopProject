package com.example.oopproject.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oopproject.Post
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentWriteBinding
import com.example.oopproject.viewModel.PostWriteViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.UUID

class writeFragment : Fragment() {

    private lateinit var binding : FragmentWriteBinding
    private lateinit var postWriteViewModel: PostWriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWriteBinding.inflate(inflater, container, false)

        postWriteViewModel = ViewModelProvider(this).get(PostWriteViewModel::class.java)

        binding.dueEditWrite.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.dueEditWrite.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }

        binding.submitWrite.setOnClickListener{
            val title = binding.editTitleWrite.text.toString()
            val description = binding.editContentsWrite.text.toString()
            val dueDate = binding.dueEditWrite.text.toString()
            val keywords = binding.keywordEditWrite.text.toString().split(",")

            if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() || keywords.isEmpty()) {
                Toast.makeText(requireContext(), "입력되지 않은 필드가 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val post = Post(
                name = title,
                keyword = keywords,
                dueDate = dueDate,
                description = description,
                apply = "NONE",
                like = "NONE",
                date = System.currentTimeMillis().toString()
            )

            postWriteViewModel.createPost(post)

            postWriteViewModel.isPostCreated.observe(viewLifecycleOwner, Observer { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(requireContext(), "게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "작성 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return binding.root
    }
}