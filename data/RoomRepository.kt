package com.example.chatroomapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class RoomRepository(private val firestore: FirebaseFirestore) {

    suspend fun createRoom(name: String): result<Unit> = try {
        val room = Room(name = name)
        firestore.collection("rooms").add(room).await()
        result.Success(Unit)
    } catch (e: Exception) {
        result.Error(e)
    }

    suspend fun getRooms(): result<List<Room>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(Room::class.java)!!.copy(id = document.id)
        }
        result.Success(rooms)
    } catch (e: Exception) {
        result.Error(e)
    }
}
