package com.example.designyourroom

class ExampleObj(obj: String, link: String) {
    private  var objText : String = obj
    private var linkObj: String = link

    fun getObjText(): String
    {
        return objText
    }

    fun getLink(): String
    {
        return linkObj
    }


}