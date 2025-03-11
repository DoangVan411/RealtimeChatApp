package com.example.realtimechatapp.domain.repository

interface NetworkRepository {
    fun isNetworkAvailable(): Boolean
}