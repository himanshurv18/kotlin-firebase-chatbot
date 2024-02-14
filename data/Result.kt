package com.example.chatroomapp.data

sealed class result<out T> {
    data class Success<out T>(val data: T) : result<T>()
    data class Error(val exception: Exception) : result<Nothing>()
}