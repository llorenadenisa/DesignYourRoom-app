package com.example.designyourroom

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.photo_select_method.*

class PhotoMethodSelector: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_select_method)
        val bottomNavigationMenu: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationMenu.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.open_camera ->{
                Log.d("Camera", "Deschide camera")
                return true
            }
            R.id.upload_gallery ->{
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

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK).apply{
            this.type="image/*"
            startActivityForResult(this, REQUEST_CODE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null && data.data != null) {
                val image = data.data
                val source = ImageDecoder.createSource(this!!.contentResolver, image!!)
                val bitmapImag = ImageDecoder.decodeBitmap(source)
                imageView.setImageBitmap(bitmapImag)
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 100
    }

}