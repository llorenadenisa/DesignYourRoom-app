package com.example.designyourroom

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.edit_photo_layout.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


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
        img_set.setImageResource(R.drawable.room_img)
        bitmap = (img_set.drawable as BitmapDrawable).bitmap
        val picEditor = EditPic(bitmap)
        img_set.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                   var editedPic = rpPaintHSV(bitmap)
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

    fun rpPaintHSV(bitmap: Bitmap): Mat {
        val cannyMinT = 30.0
        val ratio = 2.5

        val mRgbMat = Mat()
        Utils.bitmapToMat(bitmap, mRgbMat)
        Imgproc.cvtColor(mRgbMat,mRgbMat, Imgproc.COLOR_RGBA2RGB)
        val mask = Mat(Size(mRgbMat.width()/8.0, mRgbMat.height()/8.0), CvType.CV_8UC1, Scalar(0.0))

        val img = Mat()
        mRgbMat.copyTo(img)
        // grayscale
        val mGreyScaleMat = Mat()
        Imgproc.cvtColor(mRgbMat, mGreyScaleMat, Imgproc.COLOR_RGB2GRAY, 3)
        Imgproc.medianBlur(mGreyScaleMat,mGreyScaleMat,3)
        val cannyGreyMat = Mat()
        Imgproc.Canny(mGreyScaleMat, cannyGreyMat, cannyMinT, cannyMinT*ratio, 3)
        //hsv
        val hsvImage = Mat()
        Imgproc.cvtColor(img,hsvImage, Imgproc.COLOR_RGB2HSV)
        //got the hsv values
        val list = ArrayList<Mat>(3)
        Core.split(hsvImage, list)
        val sChannelMat = Mat()
        Core.merge(listOf(list.get(1)), sChannelMat)
        Imgproc.medianBlur(sChannelMat,sChannelMat,3)

        // canny
        val cannyMat = Mat()
        Imgproc.Canny(sChannelMat, cannyMat, cannyMinT, cannyMinT*ratio, 3)

        Core.addWeighted(cannyMat,0.5, cannyGreyMat,0.5 ,0.0,cannyMat)
        Imgproc.dilate(cannyMat, cannyMat,mask, Point(0.0,0.0), 5)

        val displayMetrics = DisplayMetrics()
        val height = 1800
        val width = 1000
        val seedPoint = Point(mRgbMat.width()/width.toDouble(), mRgbMat.height()/height.toDouble())
        Imgproc.resize(cannyMat, cannyMat, Size(cannyMat.width() + 2.0, cannyMat.height() + 2.0))
        Imgproc.medianBlur(mRgbMat,mRgbMat,15)
        val floodFillFlag = 8

        Imgproc.floodFill(
            mRgbMat,
            cannyMat,
            seedPoint,
            Scalar(
                Color.red(chosenColor).toDouble(),
                Color.green(chosenColor).toDouble(),
                Color.blue(chosenColor).toDouble()),
            Rect(),
            Scalar(5.0, 5.0, 5.0),
            Scalar(5.0, 5.0, 5.0),
            floodFillFlag
        )
        // showImage(mRgbMat,floodFillImage)
        Imgproc.dilate(mRgbMat, mRgbMat, mask, Point(0.0,0.0), 5)
        //got the hsv of the mask image
        val rgbHsvImage = Mat()
        Imgproc.cvtColor(mRgbMat,rgbHsvImage, Imgproc.COLOR_RGB2HSV)
        val list1 = ArrayList<Mat>(3)
        Core.split(rgbHsvImage, list1)
        //merged the “v” of original image with mRgb mat
        val result = Mat()
        Core.merge(listOf(list1.get(0),list1.get(1),list.get(2)), result)
        // converted to rgb
        Imgproc.cvtColor(result, result, Imgproc.COLOR_HSV2RGB)
        Core.addWeighted(result,0.7, img,0.3 ,0.0,result )
        return result
    }


    companion object {
        const val REQUEST_CODE = 100
    }

}