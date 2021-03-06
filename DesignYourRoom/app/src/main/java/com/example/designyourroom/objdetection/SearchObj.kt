package com.example.designyourroom.objdetection

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.designyourroom.ExampleObj
import com.example.designyourroom.ObjectsViewAdapter
import com.example.designyourroom.R
import com.example.designyourroom.RoomObjNode
import com.google.firebase.database.*

class SearchObj : AppCompatActivity() {
    lateinit var detectedObj: Array<String>
    var listOfObj : ArrayList<ExampleObj> = ArrayList()
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
                println("The read failed: " + error.message);
            }

        })


    }

    private fun checkObjectsInDb(objs:MutableList<RoomObjNode>) {


        for( obj in detectedObj) {
            val withoutSpaces = obj.replace(" ".toRegex(), "")
            for(dbObj in objs)
                if( withoutSpaces == dbObj.id)
                    listOfObj.add(ExampleObj(dbObj.id, dbObj.link))
        }
        initList(listOfObj)
    }

    private fun initList(listOfObj: ArrayList<ExampleObj>)
    {

        val listAdapter = ObjectsViewAdapter(listOfObj)
        val recyclerView = findViewById<RecyclerView>(R.id.objects).apply {
            setHasFixedSize(true)
            adapter = listAdapter
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}