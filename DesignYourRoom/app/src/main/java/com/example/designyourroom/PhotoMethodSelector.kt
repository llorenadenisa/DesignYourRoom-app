package com.example.designyourroom

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.photo_select_method.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PhotoMethodSelector: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private  val TAKE_PICTURE = 1
    private val UPLOAD_PICTURE = 2;
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    lateinit var bitmap: Bitmap

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
                Log.d("Camera", "    return trueDeschide camera")
                takePhoto()
            }
            R.id.upload_gallery -> {
                Log.d("Gallery", "Deschide galeria")
                //openGalleryForImage()
                img_set.setImageResource(R.drawable.test2)
                img_set.drawable.toBitmap()
                return true
            }
            R.id.start_edit -> {
                if (img_set.drawable == null) {
                    Log.e("noImg", "Nu exista imagine")
                    return false
                } else {
                    val absPath = saveToInternalStorage(img_set.drawable.toBitmap())
                    val intent = Intent(this, EditPicture::class.java).apply {
                        action = Intent.ACTION_SEND
                    }
                    intent.putExtra("image", absPath)
                    startActivity(intent)

                    return true
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK).apply{
            this.type="image/*"
        }
        startActivityForResult(intent, REQUEST_CODE)


    }
    private fun takePhoto() {
        if(hasNoPermissions()){
            requestPermission()
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, TAKE_PICTURE)
        }catch (e: ActivityNotFoundException){
            Log.e("camera", "nu am putut sa deschid camera")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                img_set.setImageBitmap(imageBitmap)
                Log.d("photo", "am returnat o poza")
            }
            UPLOAD_PICTURE -> if (resultCode == Activity.RESULT_OK && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                img_set.setImageBitmap(imageBitmap)
                Log.d("upload", "am incarcat o poza din galerie")
            }
        }
    }

    private fun hasNoPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions, 0)
    }

    companion object {
        const val REQUEST_CODE = 100
    }


    private fun showImage(image: Mat, view: ImageView) {
        val mBitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, mBitmap)
        view.setImageBitmap(mBitmap)
        bitmap = mBitmap
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }


}