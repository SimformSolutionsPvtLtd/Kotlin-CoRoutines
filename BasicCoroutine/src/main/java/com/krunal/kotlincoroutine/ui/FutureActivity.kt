package com.krunal.kotlincoroutine.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.krunal.kotlincoroutine.R
import com.krunal.kotlincoroutine.utils.AppConstanse
import com.krunal.kotlincoroutine.utils.await
import com.krunal.kotlincoroutine.utils.future
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture

class FutureActivity : AppCompatActivity() {
    companion object {
        const val TAG = "FutureActivity"

        fun newInstance(context: Context): Intent {
            return Intent(context, FutureActivity::class.java)
        }
    }

    val imageView: ImageView by lazy {
        findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
    }

    private fun loadImageAsync(imageURL: String): CompletableFuture<Bitmap?> = CompletableFuture.supplyAsync {
        val bitmap: Bitmap?
        try {
            val imageUrl = URL(imageURL)
            val conn = imageUrl.openConnection() as HttpURLConnection
            conn.connectTimeout = 30000
            conn.readTimeout = 30000
            conn.instanceFollowRedirects = true
            val input = conn.inputStream
            bitmap = BitmapFactory.decodeStream(input)
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "loadImageAsync Exception : $e")
            null
        }
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
            val future = future {
                val future1 =
                    loadImageAsync(AppConstanse.IMAGE_URL_1)

                val future2 =
                    loadImageAsync(AppConstanse.IMAGE_URL_2)
                combineImages(future1.await(), future2.await())
            }
            val image = future.await()
            image?.let {
                imageView.setImageBitmap(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewData()
    }

    fun loadAndCombineAsync(
        name1: String,
        name2: String
    ): CompletableFuture<Bitmap?> =
        future {
            val future1 = loadImageAsync(name1)
            val future2 = loadImageAsync(name2)
            combineImages(future1.await(), future2.await())
        }
}