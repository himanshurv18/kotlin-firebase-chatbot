package com.example.chatroomapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroomapp.Injection
import com.example.chatroomapp.data.Message
import com.example.chatroomapp.data.MessageRepository
import com.example.chatroomapp.data.User
import com.example.chatroomapp.data.UserRepository
import com.example.chatroomapp.data.result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class MessageViewModel : ViewModel() {

    private val messageRepository: MessageRepository
    private val userRepository: UserRepository
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser


    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
        loadCurrentUser()
    }


    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()


    fun setRoomId(roomId: String) {
        loadCurrentUser()
        _roomId.value = roomId
        Log.e("insideRoom","${currentUser.value}")
        loadMessages()
    }

    fun sendMessage(text: String) {
        if (_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = text
            )
            viewModelScope.launch {
                when (messageRepository.sendMessage(_roomId.value.toString(), message)) {
                    is result.Success -> Unit

                    else -> {

                    }
                }
            }
        }
    }


    fun loadMessages() {
        viewModelScope.launch {
            messageRepository.getChatMessages(_roomId.value.toString())
                .collect { _messages.value = it }
        }
    }


    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result1 = userRepository.getCurrentUser()) {
                is result.Success -> {
                    val currentUser = result1.data // Safe access
                    _currentUser.value = currentUser
                    Log.e("loadc","${_currentUser.value}")
                }

                else -> {
                    // Handle error case if needed
                }
            }

        }
    }
}
