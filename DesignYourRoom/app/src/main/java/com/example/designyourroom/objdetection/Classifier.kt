package com.example.designyourroom.objdetection

import android.graphics.Bitmap

interface Classifier {
    fun recognize(bitmap : Bitmap) : List<Recognition>
}