package com.example.realtimechatapp.presentation.utils

import android.annotation.SuppressLint
import com.example.realtimechatapp.data.source.remote.APIService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object Utils {
    fun getTime(timestamp: Date): String{
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return formatter.format(timestamp)
    }
}