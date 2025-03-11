package com.example.realtimechatapp.presentation.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.realtimechatapp.R
import com.example.realtimechatapp.data.repository.UserRepositoryImpl
import com.example.realtimechatapp.data.source.remote.FirebaseUserDataSource
import com.example.realtimechatapp.databinding.FragmentLoginBinding
import com.example.realtimechatapp.domain.usecase.LoginUseCase
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels (
        factoryProducer = {LoginViewModel.Factory}
    )
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observeLoginState()
        checkCurrentUser()
    }

    private fun setListener(){
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                if(checkInput()) {
                    viewModel.login(email, password)
                }
            }
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(viewLifecycleOwner) { login ->
            if (login == true) {
                findNavController().navigate(R.id.action_loginFragment_to_usersFragment)
            } else {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInput(): Boolean {
        val email = binding.etEmail
        val password = binding.etPassword
        when {
            email.text.toString().isEmpty() -> {
                binding.tvEmailError.visibility = View.VISIBLE
                email.requestFocus()
                return false
            }
            password.text.toString().isEmpty() -> {
                binding.tvPasswordError.visibility = View.VISIBLE
                password.requestFocus()
                return false
            }
            else -> {
                binding.tvEmailError.visibility = View.GONE
                binding.tvPasswordError.visibility = View.GONE
                return true
            }
        }
    }

    private fun checkCurrentUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_usersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}