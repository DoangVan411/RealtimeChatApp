package com.example.realtimechatapp.domain.usecase

import com.example.realtimechatapp.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return userRepository.login(email, password)
    }
}