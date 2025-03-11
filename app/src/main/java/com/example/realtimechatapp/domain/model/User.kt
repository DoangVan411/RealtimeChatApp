package com.example.realtimechatapp.domain.model

data class User (
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val fcmToken: String = ""
)