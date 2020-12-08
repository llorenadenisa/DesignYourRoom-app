package com.example.designyourroom

import android.graphics.Bitmap
import android.graphics.Color
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class WallDetection() {

    val chosenColor = Color.RED

    fun applyPaint(bitmap: Bitmap): Mat {
        val cannyMinTreshold = 20.0
        //rgba->rgba2->grayscale pt edge detection ->canny edge->from grayscale to rgb->floodfill

        val mRgbMat = Mat()
        //conversie din bmp in mat
        Utils.bitmapToMat(bitmap, mRgbMat)
        
	    Imgproc.cvtColor(mRgbMat,mRgbMat, Imgproc.COLOR_RGBA2RGB)
       
	    val mask = Mat(Size(mRgbMat.width().toDouble(), mRgbMat.height().toDouble()), CvType.CV_8UC1, Scalar(0.0))

        val img = Mat()
        mRgbMat.copyTo(img)
        
	    // grayscale
        val mGreyScaleMat = Mat()
        Imgproc.cvtColor(mRgbMat, mGreyScaleMat, Imgproc.COLOR_RGB2GRAY, 3)
        Imgproc.medianBlur(mGreyScaleMat,mGreyScaleMat,3)
        
	    val cannyGreyMat = Mat()
        Imgproc.Canny(mGreyScaleMat, cannyGreyMat, cannyMinTreshold, 90.0, 3)
        
	    //hsv
        val hsvImage = Mat()
        Imgproc.cvtColor(img,hsvImage, Imgproc.COLOR_RGB2HSV)
        
	    //get the hsv values
        val list = ArrayList<Mat>(3)
        Core.split(hsvImage, list)
        
	    val sChannelMat = Mat()
        Core.merge(listOf(list[1]), sChannelMat)
	    Imgproc.medianBlur(sChannelMat,sChannelMat,3)

        // canny edge detect
        val cannyMat = Mat()
        Imgproc.Canny(sChannelMat, cannyMat, cannyMinTreshold,90.0, 3)
        
	    Core.addWeighted(cannyMat,0.6, cannyGreyMat,0.4 ,0.0,cannyMat)
        Imgproc.dilate(cannyMat, cannyMat,mask, Point(0.0,0.0), 5)

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

        Imgproc.dilate(mRgbMat, mRgbMat, mask, Point(0.0,0.0), 5)

       //alpha blending

        val rgbHsvImage = Mat()
        Imgproc.cvtColor(mRgbMat,rgbHsvImage, Imgproc.COLOR_RGB2HSV)
        val list1 = ArrayList<Mat>(3)
        Core.split(rgbHsvImage, list1)

        val result = Mat()
        Core.merge(listOf(list1[0], list1[1], list[2]), result)

        Imgproc.cvtColor(result, result, Imgproc.COLOR_HSV2RGB)
        Core.addWeighted(result,0.6, img,0.4 ,0.0,result )
        return result;
    }

}