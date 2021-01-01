package com.example.designyourroom.objdetection

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.designyourroom.R
import com.example.designyourroom.RoomObjNode
import com.google.firebase.database.*

class SearchObj : AppCompatActivity() {
    lateinit var detectedObj: Array<String>
    lateinit var ref: DatabaseReference
    lateinit var objList: MutableList<RoomObjNode>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.obj_list)
        val intentRes = intent.getStringExtra("list_of_ob")

        if (intentRes != null) {
            detectedObj = intentRes.split(",").toTypedArray()
        }

        ref = FirebaseDatabase.getInstance().reference.child("room_obj")
        objList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                objList.clear()
                for (obj in snapshot.children)
                {
                    val objNode = RoomObjNode(obj.key as String, obj.value as String)
                    objList.add(objNode)
                }

                checkObjectsInDb(objList)

            }

            override fun onCancelled(error: DatabaseError) {
                System.out.println("The read failed: " + error.message);
            }

        })





//        val arrayAdapter: ArrayAdapter<*>
//        val mListView = findViewById<ListView>(R.id.objects)
//        arrayAdapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_list_item_1, detectedObj
//        )
//        mListView.adapter = arrayAdapter
    }

    private fun checkObjectsInDb(obj:MutableList<RoomObjNode>) {

//        for( objectsDetected in detectedObj) {
//            for (objInDb in objList) {
//                if (objectsDetected == objInDb.id) {
//                    Log.e("Gasit", objInDb.id + objInDb.link)
//                }
//            }
//        }

        }
    }

}