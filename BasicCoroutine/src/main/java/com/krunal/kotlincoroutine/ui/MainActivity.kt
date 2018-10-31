package com.krunal.kotlincoroutine.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.krunal.kotlincoroutine.R

class MainActivity : AppCompatActivity() {

    private val btnThread: Button by lazy {
        findViewById<Button>(R.id.btn_thread)
    }

    private val btnCallbacks: Button by lazy {
        findViewById<Button>(R.id.btn_callbacks)
    }

    private val btnPromise: Button by lazy {
        findViewById<Button>(R.id.btn_promise)
    }

    private val btnSuspend: Button by lazy {
        findViewById<Button>(R.id.btn_suspend)
    }

    private val btnAsync: Button by lazy {
        findViewById<Button>(R.id.btn_async)
    }

    private val btnFuture: Button by lazy {
        findViewById<Button>(R.id.btn_future)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnThread.setOnClickListener {
            startActivity(ThreadsActivity.newInstance(this@MainActivity))
        }

        btnCallbacks.setOnClickListener {
            startActivity(CallbacksActivity.newInstance(this@MainActivity))
        }

        btnPromise.setOnClickListener {
            startActivity(PromiseActivity.newInstance(this@MainActivity))
        }

        btnSuspend.setOnClickListener {
            startActivity(SuspendActivity.newInstance(this@MainActivity))
        }

        btnAsync.setOnClickListener {
            startActivity(AsyncActivity.newInstance(this@MainActivity))
        }

        btnFuture.setOnClickListener {
            startActivity(FutureActivity.newInstance(this@MainActivity))
        }
    }
}
