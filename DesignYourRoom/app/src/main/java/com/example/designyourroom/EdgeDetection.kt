package com.example.designyourroom

import android.R.attr.bitmap
import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class EdgeDetection {

    fun detectEdges(bmp: Bitmap)
    {
        val rgba = Mat()
        Utils.bitmapToMat(bmp, rgba)

        val edges = Mat(rgba.size(), CvType.CV_8UC1)
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4)
        Imgproc.Canny(edges, edges, 80.0, 100.0)

    }
}