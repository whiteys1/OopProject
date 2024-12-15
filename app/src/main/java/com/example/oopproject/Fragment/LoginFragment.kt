package com.example.oopproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentLoginBinding
import com.example.oopproject.viewModel.SignInViewModel


class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        binding.loginBtnLogin.setOnClickListener {
            val email = binding.emailEditLogin.text.toString().trim()
            val password = binding.passEditLogin.text.toString()

            if ( email.isEmpty() || password.isEmpty() ) {
                Toast.makeText(requireContext(), "입력하지 않은 필드가 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signInViewModel.login(email, password)
        }
        binding.signinBtnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }

        signInViewModel.loginStatus.observe(viewLifecycleOwner, Observer { task ->
            when ( task ) {
                "로그인 성공" -> {
                    Toast.makeText(requireContext(), "로그인 성공!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                "로그인 실패" -> {
                    Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "입력하신 이메일과 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }


}