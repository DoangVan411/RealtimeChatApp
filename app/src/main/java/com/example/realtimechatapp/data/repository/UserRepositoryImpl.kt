package com.example.realtimechatapp.data.repository

import com.example.realtimechatapp.data.source.remote.FirebaseUserDataSource
import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val firebaseUserDataSource: FirebaseUserDataSource): UserRepository {
    override suspend fun getUserName(uid: String): String {
        return firebaseUserDataSource.getUserName(uid)
    }

    override suspend fun getUsers(): Flow<List<User>> {
        return firebaseUserDataSource.getUsers()
    }

    override suspend fun register(email: String, password: String, name: String): Boolean {
        return firebaseUserDataSource.register(email, password, name)
    }

    override suspend fun login(email: String, password: String): Boolean {
        return firebaseUserDataSource.login(email, password)
    }
}