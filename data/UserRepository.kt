package com.example.chatroomapp.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val auth: FirebaseAuth,
                     private val firestore: FirebaseFirestore
){
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            //add user to firestore
            val user = User(firstName, lastName, email)
            saveUserToFirestore(user)
            result.Success(true)
        } catch (e: Exception) {
            result.Error(e)
        }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun login(email: String, password: String): result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            result.Success(true)
        } catch (e: Exception) {
            result.Error(e)
        }
    suspend fun getCurrentUser(): result<User> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()
            val user = userDocument.toObject(User::class.java)
            if (user != null) {
                Log.d("user2","$uid")
                result.Success(user)
            } else {
                result.Error(Exception("User data not found"))
            }
        } else {
            result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        result.Error(e)
    }







}