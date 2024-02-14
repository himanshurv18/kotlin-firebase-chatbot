package com.example.chatroomapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroomapp.Injection
import com.example.chatroomapp.data.Room
import com.example.chatroomapp.data.RoomRepository
import com.example.chatroomapp.data.result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RoomViewModel() : ViewModel() {



    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms
    private val roomRepository: RoomRepository
    init {
        roomRepository = RoomRepository(Injection.instance())
        loadRooms()
    }

    fun createRoom(name: String) {
        viewModelScope.launch {
            roomRepository.createRoom(name)
            loadRooms()
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            when (val result1 = roomRepository.getRooms()) {
                is result.Success -> {
                    val rooms = result1.data
                    // Check if rooms is not null before assigning its value to _rooms.value
                    _rooms.value = rooms
                }

                else -> {
                }
            }
        }
    }



    fun logout() {
        // Perform logout operations such as clearing user session, etc.
        // You can use Firebase Auth or any other authentication library you are using.
        // For example, if using Firebase Auth:
        FirebaseAuth.getInstance().signOut()

    }


}