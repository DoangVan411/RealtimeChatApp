package com.example.realtimechatapp

import android.app.Application
import com.example.realtimechatapp.data.source.remote.FirebaseChatDataSource
import com.example.realtimechatapp.data.source.remote.FirebaseUserDataSource
import com.example.realtimechatapp.di.NetworkModule
import com.example.realtimechatapp.di.RepositoryModule

class RealtimeChatAppApplication: Application() {
    lateinit var networkModule: NetworkModule
    lateinit var repositoryModule: RepositoryModule

    override fun onCreate() {
        super.onCreate()
        networkModule = NetworkModule()
        repositoryModule = RepositoryModule().also {
            it.provideChatRepository(FirebaseChatDataSource(networkModule.firestore))
            it.provideNetworkRepository(applicationContext)
            it.provideNotificationRepository(networkModule.provideAPIService(networkModule.getRetrofit()))
            it.provideUserRepository(FirebaseUserDataSource(networkModule.auth, networkModule.firestore))
        }
    }
}