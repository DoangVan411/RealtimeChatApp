package com.example.realtimechatapp.di

import android.content.Context
import com.example.realtimechatapp.BuildConfig
import com.example.realtimechatapp.data.source.remote.APIService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule() {
    private val baseURL = BuildConfig.BASE_URL

    fun getRetrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun provideAPIService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
}