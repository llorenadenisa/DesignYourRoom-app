package com.example.designyourroom

import android.R
import android.graphics.BitmapFactory
import android.os.Bundle
import kotlinx.android.synthetic.main.edit_photo.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class EditPicture: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.designyourroom.R.layout.edit_photo)
        val intent = getIntent()
        val imageStr = intent.getStringExtra("image")
        if (imageStr != null) {
            loadImageFromStorage(imageStr)
        }

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

    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "profile.jpg")
            val bmp = BitmapFactory.decodeStream(FileInputStream(f))
            edit_img_view.setImageBitmap(bmp)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


}