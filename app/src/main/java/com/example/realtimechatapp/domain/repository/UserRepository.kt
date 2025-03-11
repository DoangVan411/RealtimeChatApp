package com.example.realtimechatapp.domain.repository

import com.example.realtimechatapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserName(uid: String): String
    suspend fun getUsers(): Flow<List<User>>
    suspend fun register(email: String, password: String, name: String): Boolean
    suspend fun login(email: String, password: String): Boolean
}