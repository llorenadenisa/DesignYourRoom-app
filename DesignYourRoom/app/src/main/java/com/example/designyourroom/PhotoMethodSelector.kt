package com.example.designyourroom

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.edit_photo_layout.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class PhotoMethodSelector: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {



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
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun openGalleryForImage() {
//        val intent = Intent(Intent.ACTION_PICK).apply{
//            this.type="image/*"
//            startActivityForResult(this, REQUEST_CODE)
//        }
        img_set.setImageResource(R.drawable.room_img)
        val imageBmp = (img_set.drawable as BitmapDrawable).bitmap
        detectEdges(imageBmp)

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
//    private fun detectEdges(bmp : Bitmap)
//    {
//        val rgba = Mat()
//        Log.d("rgb",rgba.toString())
//
//        Utils.bitmapToMat(bmp, rgba)
//        val edges = Mat(rgba.size(), CvType.CV_8UC1)
//        Log.d("edges", edges.toString())
//        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4)
//        Imgproc.Canny(edges, edges, 80.0, 100.0)
//
//        showMat(img_set,edges)
//
//    }
//    private fun showMat(view: ImageView, source: Mat) {
//        Log.d("showMat", "Am ajuns in functia showMat")
//        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
//        bitmap.density = 360
//        Utils.matToBitmap(source, bitmap)
//        view.setImageBitmap(bitmap)
//    }

    private fun imgToBitmap(image: ImageFormat):Bitmap
    {
        return (img_set.drawable as BitmapDrawable).bitmap
    }



    companion object {
        const val REQUEST_CODE = 100
    }

}