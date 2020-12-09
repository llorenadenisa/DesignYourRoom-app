package com.example.designyourroom

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.edit_photo.*
import org.opencv.android.Utils
import org.opencv.core.Mat
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class EditPicture: AppCompatActivity() {
    var mDefaultColor = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.designyourroom.R.layout.edit_photo)
        val intent = getIntent()
        val imageStr = intent.getStringExtra("image")
        if (imageStr != null) {
            loadImageFromStorage(imageStr)
        }


        colors.setOnClickListener {
                   openColorPicker()
        }

    }

    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "profile.jpg")
            val bmp = BitmapFactory.decodeStream(FileInputStream(f))
            edit_img_view.setImageBitmap(bmp)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun openColorPicker() {
        val colorPicker = AmbilWarnaDialog(this, mDefaultColor, object : OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog) {}
            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                mDefaultColor = color
               Log.i("color", "Culoarea a fost aleasa")
                val wallDet = WallDetection(mDefaultColor)
                val editPic = wallDet.applyPaint(edit_img_view.drawable.toBitmap())
                showImage(editPic, edit_img_view)

            }
        })
        colorPicker.show()
    }

        private fun showImage(image: Mat, view: ImageView) {
        val mBitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, mBitmap)
        view.setImageBitmap(mBitmap)
    }

}