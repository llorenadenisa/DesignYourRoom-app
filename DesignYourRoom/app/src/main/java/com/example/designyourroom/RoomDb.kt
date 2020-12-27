package com.example.designyourroom

import android.util.Log
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RoomDb {

    private val database = FirebaseDatabase.getInstance().reference.child("room_obj")

    suspend fun getAll(): List<RoomObj> = suspendCoroutine { continuation ->
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                database.removeEventListener(this)
                continuation.resumeWithException(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val values = mutableListOf<RoomNode>()

                val children = p0.children

                //TODO ("Iterate through the children and get the node value")
                for(childIterator in children) {
                    values.add(RoomNode(childIterator.child("id").value as String,
                        childIterator.child("link").value as String))
                }

               val listOfObj = values as ArrayList<RoomNode>

                for (obj in listOfObj)
                    Log.e("obj", obj.id)

                database.removeEventListener(this)

                continuation.resume(values.map { chiuitNode -> chiuitNode.toDomainModel() })
            }

        })
    }
    fun RoomNode.toDomainModel(): RoomObj {
        return RoomObj(id, link)
    }


}