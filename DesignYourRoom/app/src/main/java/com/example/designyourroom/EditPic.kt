package com.example.designyourroom
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EditPic : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_photo_layout)

    }

    fun processImage(bmp:Bitmap)
    {
        val edgeDet = EdgeDetection()
        edgeDet.detectEdges(bmp)

    }

//    fun checkIfAppSupportsDepthApi()
//    {
//        val config = session.config
//
//        val isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)
//        if (isDepthSupported) {
//            config.depthMode = Config.DepthMode.AUTOMATIC
//        }
//        session.configure(config)
 //   }

}