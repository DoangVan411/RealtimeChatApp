package com.example.realtimechatapp.data.source.remote

import android.content.Context
import android.content.res.AssetManager
import android.content.res.AssetManager.AssetInputStream
import com.example.realtimechatapp.RealtimeChatAppApplication
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.io.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

object AccessToken {
    private const val SCOPES = "https://www.googleapis.com/auth/firebase.messaging"
    suspend fun getAccessToken(context: Context): String? {
        return withContext(Dispatchers.IO) {
            val stream: InputStream = context.assets.open("service-account.json")
            val googleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(SCOPES))
            googleCredentials.refresh()
            googleCredentials.accessToken.tokenValue
        }
    }

}