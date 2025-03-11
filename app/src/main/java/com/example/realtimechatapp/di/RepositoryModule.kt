package com.example.realtimechatapp.di

import android.content.Context
import com.example.realtimechatapp.data.repository.ChatRepositoryImpl
import com.example.realtimechatapp.data.repository.NetworkRepositoryImpl
import com.example.realtimechatapp.data.repository.NotificationRepositoryImpl
import com.example.realtimechatapp.data.repository.UserRepositoryImpl
import com.example.realtimechatapp.data.source.remote.APIService
import com.example.realtimechatapp.data.source.remote.FirebaseChatDataSource
import com.example.realtimechatapp.data.source.remote.FirebaseUserDataSource
import com.example.realtimechatapp.domain.repository.ChatRepository
import com.example.realtimechatapp.domain.repository.NetworkRepository
import com.example.realtimechatapp.domain.repository.NotificationRepository
import com.example.realtimechatapp.domain.repository.UserRepository
import retrofit2.Retrofit

class RepositoryModule {
    lateinit var chatRepository: ChatRepository
    lateinit var networkRepository: NetworkRepository
    lateinit var notificationRepository: NotificationRepository
    lateinit var userRepository: UserRepository

    fun provideChatRepository(firebaseChatDataSource: FirebaseChatDataSource) {
        chatRepository = ChatRepositoryImpl(firebaseChatDataSource)
    }

    fun provideNetworkRepository(context: Context) {
        networkRepository = NetworkRepositoryImpl(context)
    }

    fun provideNotificationRepository(apiService: APIService) {
        notificationRepository = NotificationRepositoryImpl(apiService)
    }

    fun provideUserRepository(firebaseUserRepository: FirebaseUserDataSource) {
        userRepository = UserRepositoryImpl(firebaseUserRepository)
    }
}