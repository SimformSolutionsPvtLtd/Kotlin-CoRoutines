package com.krunal.kotlincoroutine.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dandan.jsonhandleview.library.JsonViewLayout
import com.krunal.kotlincoroutine.R
import com.krunal.kotlincoroutine.utils.AppConstanse
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SuspendActivity : AppCompatActivity() {
    companion object {
        const val TAG = "SuspendActivity"

        fun newInstance(context: Context): Intent {
            return Intent(context, SuspendActivity::class.java)
        }
    }

    private var job = Job()
    val jsonView: JsonViewLayout by lazy {
        findViewById<JsonViewLayout>(R.id.jsonView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)
    }

    private suspend fun fetchTopStories() = suspendCoroutine<JSONArray?> { cont ->
        GlobalScope.async {
            try {
                val response =
                    AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.TOP_STORIES}")

                if (response != null) {
                    cont.resume(JSONArray(response))
                } else {
                    cont.resumeWithException(RuntimeException("Fail to get response"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchTopStories Exception : $e")
                cont.resumeWithException(e)
            }
        }
    }

    private suspend fun fetchNewsStoriesDetail(storyId: String?) = suspendCoroutine<JSONObject?> { cont ->
        GlobalScope.async {
            try {
                if (storyId != null) {
                    val response =
                        AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.getNewsStoryDetail(storyId)}")

                    if (response != null) {
                        cont.resume(JSONObject(response))
                    } else {
                        cont.resumeWithException(RuntimeException("Fail to get response"))
                    }
                } else {
                    cont.resumeWithException(RuntimeException("storyId null"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchNewsStoriesDetail Exception : $e")
                cont.resumeWithException(e)
            }
        }
    }

    private fun viewData() {
        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                val topStories = fetchTopStories()
                val newsStoriesDetail = fetchNewsStoriesDetail(topStories?.getString(0))
                newsStoriesDetail?.let {
                    jsonView.bindJson(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "launch Exception : $e")
            }
        }
        job.invokeOnCompletion {
            onCleared()
        }
    }

    override fun onResume() {
        super.onResume()
        viewData()
    }

    override fun onPause() {
        onCleared()
        super.onPause()
    }

    private fun onCleared() {
        job.cancel()
    }
}