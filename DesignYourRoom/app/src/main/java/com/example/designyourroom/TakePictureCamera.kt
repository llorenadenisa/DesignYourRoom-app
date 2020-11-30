//package com.example.designyourroom
//
//import android.R
//import android.app.Activity
//import android.content.ContentResolver
//import android.content.Intent
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Environment
//import android.provider.MediaStore
//import android.view.View
//import android.widget.Toast
//import androidx.core.app.ActivityCompat.startActivityForResult
//import java.io.File
//
//
//private const val TAKE_PICTURE = 1
//private var imageUri : Uri? = null
//
//class TakePicture {
//    fun takePhoto(view: View?) {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val photo = File(Environment.getExternalStorageDirectory(), "Pic.jpg")
//        intent.putExtra(
//            MediaStore.EXTRA_OUTPUT,
//            Uri.fromFile(photo)
//        )
//        imageUri = Uri.fromFile(photo)
//        startActivityForResult(intent, TAKE_PICTURE)
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK) {
//                val selectedImage = imageUri!!
//                getContentResolver().notifyChange(selectedImage, null)
//                val imageView: ImageView = findViewById(R.id.ImageView) as ImageView
//                val cr: ContentResolver = getContentResolver()
//                val bitmap: Bitmap
//                try {
//                    bitmap = MediaStore.Images.Media
//                        .getBitmap(cr, selectedImage)
//                    imageView.setImageBitmap(bitmap)
//                    Toast.makeText(
//                        this, selectedImage.toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
//                } catch (e: Exception) {
//                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
//                        .show()
//                    Log.e("Camera", e.toString())
//                }
//            }
//        }
//    }
//
//}