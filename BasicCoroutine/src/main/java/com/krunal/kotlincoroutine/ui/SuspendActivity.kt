package com.krunal.kotlincoroutine.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dandan.jsonhandleview.library.JsonViewLayout
import com.krunal.kotlincoroutine.R
import com.krunal.kotlincoroutine.utils.AppConstanse
import com.krunal.kotlincoroutine.utils.doAsync
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

    private suspend fun fetchTopStories(): JSONArray? {
        return suspendCancellableCoroutine {
            doAsync {
                try {
                    val response =
                        AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.TOP_STORIES}")

                    if (response != null) {
                        it.resume(JSONArray(response))
                    } else {
                        it.resumeWithException(RuntimeException("Fail to get response"))
                    }
                } catch (e: Exception) {
                    Log.e(PromiseActivity.TAG, "FetchTopStories Exception : $e")
                    it.resumeWithException(e)
                }
            }
        }
    }

    private suspend fun fetchNewsStoriesDetail(storyId: String?): JSONObject? {
        return suspendCancellableCoroutine {
            doAsync {
                try {
                    if (storyId != null) {
                        val response =
                            AppConstanse.getApiMethod(
                                "${AppConstanse.API_BASE_URL}${AppConstanse.getNewsStoryDetail(
                                    storyId
                                )}"
                            )

                        if (response != null) {
                            it.resume(JSONObject(response))
                        } else {
                            it.resumeWithException(RuntimeException("Fail to get response"))
                        }
                    } else {
                        it.resumeWithException(RuntimeException("storyId null"))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "FetchNewsStoriesDetail Exception : $e")
                    it.resumeWithException(e)
                }
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