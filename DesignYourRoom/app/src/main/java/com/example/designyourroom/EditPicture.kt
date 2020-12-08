package com.example.designyourroom

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.edit_photo_layout.*
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.lang.reflect.Field


class EditPicture: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_photo_layout)
        val intent = getIntent()
        val imageStr = intent.getStringExtra("image")
        val imgUtil = ImageUtil()
        val image = imageStr?.let { imgUtil.convertFromBase64(it) }
        edit_img_view.setImageBitmap(image)

//        val fields: Array<Field> = Class.forName("$packageName.R\$color").declaredFields
//
//        for (field in fields) {
//            val colorName: String = field.getName()
//            val colorId: Int = field.getInt(null)
//            val color = resources.getColor(colorId)
//            Log.i("test", "$colorName => $colorId => $color")
//        }


    }

//    private fun showImage(image: Mat, view: ImageView) {
//        val mBitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(image, mBitmap)
//        view.setImageBitmap(mBitmap)
//        bitmap = mBitmap
//    }


}