package com.example.realtimechatapp.data.source.remote

import android.app.Application
import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.presentation.getFcmToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseUserDataSource(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore

) {

    suspend fun getUserName(uid: String): String {
        return firestore.collection("users").document(uid).get().await().getString("name") ?: "Unknown"
    }

    fun getUsers(): Flow<List<User>> = callbackFlow {
        val listener =  firestore.collection("users")
            .addSnapshotListener { snapshot, _ ->
                val userList = snapshot?.toObjects(User::class.java) ?: emptyList()
                trySend(userList)
            }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun register(email: String, password: String, name: String): Boolean {
         return try {
             val result = auth.createUserWithEmailAndPassword(email, password).await()
             val uid = result.user?.uid ?: return false
             val fcmToken = getFcmToken()

             val user = User(uid, name, email, fcmToken)
             firestore.collection("users").document(uid).set(user).await()

             true
         }
         catch (e: Exception) {
            false
         }
    }

    suspend fun login(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user != null
        } catch (e: Exception) {
            false
        }
    }

}
