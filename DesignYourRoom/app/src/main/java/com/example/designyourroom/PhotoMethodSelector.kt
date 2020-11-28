package com.example.designyourroom

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.edit_photo_layout.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*



class PhotoMethodSelector: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var bitmap: Bitmap
    var chosenColor = Color.GREEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_select_method)
        OpenCVLoader.initDebug();
        val bottomNavigationMenu: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationMenu.setOnNavigationItemSelectedListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.open_camera -> {
                Log.d("Camera", "Deschide camera")
                return true
            }
            R.id.upload_gallery -> {
                Log.d("Gallery", "Deschide galeria")
                openGalleryForImage()
                return true
            }
            R.id.upload_cloud -> {
                Log.d("Cloud", "Upload din cloud")
                return true
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    private fun openGalleryForImage() {
//        val intent = Intent(Intent.ACTION_PICK).apply{
//            this.type="image/*"
//            startActivityForResult(this, REQUEST_CODE)
//        }
        img_set.setImageResource(R.drawable.test6)
        bitmap = (img_set.drawable as BitmapDrawable).bitmap
        val picEditor = EditPic(bitmap)
        img_set.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                   var editedPic = picEditor.applyPaint(bitmap)
                    showImage(editedPic, img_set)

            }
            true
            }

    }
    private fun showImage(image: Mat, view: ImageView) {
        val mBitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, mBitmap)
        view.setImageBitmap(mBitmap)
        bitmap = mBitmap
    }

    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
//            if (data != null && data.data != null) {
//                val image = data.data
//                val source = ImageDecoder.createSource(this.contentResolver, image!!)
//                val bitmapImag = ImageDecoder.decodeBitmap(source)
//                val editPic = EditPic()
//                editPic.processImage(bitmapImag)
//            }
//        }
//    }



    companion object {
        const val REQUEST_CODE = 100
    }

}