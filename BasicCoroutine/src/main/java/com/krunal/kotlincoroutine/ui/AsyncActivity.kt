package com.krunal.kotlincoroutine.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.krunal.kotlincoroutine.R
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import com.krunal.kotlincoroutine.utils.AppConstanse
import com.krunal.kotlincoroutine.utils.async
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class AsyncActivity : AppCompatActivity() {

    companion object {
        const val TAG = "AsyncActivity"

        fun newInstance(context: Context): Intent {
            return Intent(context, AsyncActivity::class.java)
        }
    }

    val imageView: ImageView by lazy {
        findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
    }

    private fun loadImageAsync(imageURL: String): Deferred<Bitmap?> = async {
        var bitmap: Bitmap? = null
        try {
            val imageUrl = URL(imageURL)
            val conn = imageUrl.openConnection() as HttpURLConnection
            conn.connectTimeout = 30000
            conn.readTimeout = 30000
            conn.instanceFollowRedirects = true
            val input = conn.inputStream
            bitmap = BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e(TAG, "loadImageAsync Exception : $e")
        }

        return@async bitmap
    }

    private fun combineImages(image1: Bitmap?, image2: Bitmap?): Bitmap? {
        return if (image1 != null && image2 != null) {
            val bmOverlay = Bitmap.createBitmap(image1.width, image1.height, image1.config)
            val canvas = Canvas(bmOverlay)
            canvas.drawBitmap(image1, Matrix(), null)

            val marginLeft = (image1.width * 0.5 - image2.width * 0.5).toFloat()
            val marginTop = (image1.height * 0.5 - image2.height * 0.5).toFloat()
            canvas.drawBitmap(image2, marginLeft, marginTop, null)

            bmOverlay
        } else {
            null
        }
    }

    private fun viewData() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferred1 = loadImageAsync(AppConstanse.IMAGE_URL_1)
            val deferred2 = loadImageAsync(AppConstanse.IMAGE_URL_2)

            val image1 = deferred1.await()
            val image2 = deferred2.await()

            val image = combineImages(image1, image2)
            image?.let {
                imageView.setImageBitmap(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewData()
    }
}