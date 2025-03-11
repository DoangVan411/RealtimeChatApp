package com.example.realtimechatapp.presentation.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.realtimechatapp.data.repository.UserRepositoryImpl
import com.example.realtimechatapp.data.source.remote.FirebaseUserDataSource
import com.example.realtimechatapp.databinding.FragmentRegisterBinding
import com.example.realtimechatapp.domain.usecase.RegisterUseCase

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels (
        factoryProducer = {RegisterViewModel.Factory}
    )

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = binding.etName
        etEmail = binding.etEmail
        etPassword = binding.etPassword

        setListener()
        observeRegisterState()
    }

    private fun setListener() {
        with(binding) {
            btnRegister.setOnClickListener {
                if(checkInput()) {
                    viewModel.register(etEmail.text.toString(), etPassword.text.toString(), etName.text.toString())
                }
            }
            tvLogin.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeRegisterState() {
        viewModel.registerState.observe(viewLifecycleOwner) {register ->
            if(register) {
                findNavController().popBackStack()
            }
            else {
                Toast.makeText(requireContext(), "Register failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInput(): Boolean {
        val tvNameError = binding.tvNameError
        val tvEmailError = binding.tvEmailError
        val tvPasswordError = binding.tvPasswordError
        when {
            etName.text.toString().isEmpty() -> {
                tvNameError.visibility = View.VISIBLE
                etName.requestFocus()
                return false
            }
            etEmail.text.toString().isEmpty() -> {
                tvEmailError.visibility = View.VISIBLE
                etEmail.requestFocus()
                return false
            }
            etPassword.text.toString().isEmpty() -> {
                tvPasswordError.visibility = View.VISIBLE
                etPassword.requestFocus()
                return false
            }
            else -> {
                tvNameError.visibility = View.GONE
                tvEmailError.visibility = View.GONE
                tvPasswordError.visibility = View.GONE
                return true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}