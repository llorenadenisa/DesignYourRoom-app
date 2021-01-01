package com.example.designyourroom

 class RoomObjNode(val id: String, val link: String) {
    constructor():this(" ", " ")

     @JvmName("getId1")
     fun getId():String{
         return id
     }
}