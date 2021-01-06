package com.example.designyourroom

import TFLiteClassifier
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.designyourroom.objdetection.Classifier
import com.example.designyourroom.objdetection.SearchObj
import kotlinx.android.synthetic.main.edit_photo.*
import org.opencv.android.Utils
import org.opencv.core.Mat
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.OutputStream


class EditPicture: AppCompatActivity() {
    val MODEL_PATH = "mobilenet_v1_0.5_224_quant.tflite"
    val LABEL_PATH = "labels_mobilenet_quant_v1_224.txt"
    val INPUT_SIZE = 224
    var mDefaultColor = 0


    lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_photo)


        val intent = intent
        val imageStr = intent.getStringExtra("image")

        classifier = TFLiteClassifier.create(
            assets, MODEL_PATH, LABEL_PATH, INPUT_SIZE
        )

        if (imageStr != null) {
            loadImageFromStorage(imageStr)
        }


        colors.setOnClickListener {
            openColorPicker()
        }


        detect_obj.setOnClickListener{
            val img = edit_img_view.drawable.toBitmap()
            val bmp = Bitmap.createScaledBitmap(img, INPUT_SIZE, INPUT_SIZE, false)
            detectObjects(bmp)

        }

        share.setOnClickListener{
            val img = edit_img_view.drawable.toBitmap()
            shareImage(img)
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

    private fun openColorPicker()  {
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

    private  fun detectObjects(bitmap: Bitmap)
    {

        val result = classifier.recognize(bitmap)
        val str = result.joinToString { result-> result.label }

        val intent = Intent(this, SearchObj::class.java).apply {
            action = Intent.ACTION_SEND
        }
        intent.putExtra("list_of_ob", str)
        startActivity(intent)
    }

    private fun shareImage(bmp: Bitmap)
    {
        val icon: Bitmap = bmp
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(Images.Media.TITLE, "title")
        values.put(Images.Media.MIME_TYPE, "image/jpeg")
        val uri: Uri? = contentResolver.insert(
            Images.Media.EXTERNAL_CONTENT_URI,
            values
        )


        val outstream: OutputStream?
        try {
            outstream = uri?.let { contentResolver.openOutputStream(it) }
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream?.close()
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, "Share Image"))
    }

}