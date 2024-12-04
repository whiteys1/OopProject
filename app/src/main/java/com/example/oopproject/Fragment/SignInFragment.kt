package com.example.oopproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentSignInBinding
import com.example.oopproject.viewModel.PostWriteViewModel
import com.example.oopproject.viewModel.SignInViewModel


class SignInFragment : Fragment() {

    private lateinit var binding : FragmentSignInBinding
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        binding.submitSignin.setOnClickListener {
            val email = binding.emailEditSignIn.text.toString().trim()
            val nickname = binding.nicknameEditSignIn.text.toString().trim()
            val password = binding.passEditSignin.text.toString()
            val repassPassword = binding.repassEditSignin.text.toString()

            if ( email.isEmpty() || nickname.isEmpty() || password.isEmpty() || repassPassword.isEmpty() ) {
                Toast.makeText(requireContext(), "입력 하지 않은 곳이 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if ( password != repassPassword ) {
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signInViewModel.signUp(email, password, nickname)
        }

        signInViewModel.signUpStatus.observe(viewLifecycleOwner) { task ->
            task.onSuccess {
                Toast.makeText(requireContext(), "회원가입 성공", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
            }.onFailure { error ->
                Toast.makeText(requireContext(), "회원가입 실패", Toast.LENGTH_SHORT)
            }
        }

        binding.backButtonSignIn.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
        }

        return binding.root

    }



}
