package com.krunal.kotlincoroutine.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dandan.jsonhandleview.library.JsonViewLayout
import com.krunal.kotlincoroutine.R
import com.krunal.kotlincoroutine.utils.*
import org.json.JSONArray
import org.json.JSONObject

class PromiseActivity : AppCompatActivity() {
    companion object {
        const val TAG = "PromiseActivity"

        fun newInstance(context: Context): Intent {
            return Intent(context, PromiseActivity::class.java)
        }
    }

    val jsonView: JsonViewLayout by lazy {
        findViewById<JsonViewLayout>(R.id.jsonView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)
    }

    private fun fetchTopStories(): Promise<JSONArray?> {
        return doAsyncResultPromise {
            try {
                val response =
                    AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.TOP_STORIES}")

                if (response != null) {
                    return@doAsyncResultPromise JSONArray(response)
                } else {
                    throw RuntimeException("Fail to get response")
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchTopStories Exception : $e")
                throw e
            }
        }
    }

    private fun fetchNewsStoriesDetail(storyId: String?): Promise<JSONObject?> {
        return doAsyncResultPromise {
            try {
                if (storyId != null) {
                    val response =
                        AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.getNewsStoryDetail(storyId)}")

                    if (response != null) {
                        return@doAsyncResultPromise JSONObject(response)
                    } else {
                        throw RuntimeException("Fail to get response")
                    }
                } else {
                    throw RuntimeException("storyId null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchNewsStoriesDetail Exception : $e")
                throw e
            }
        }
    }

    private fun viewData() {
        fetchTopStories()
            .thenCompose { topStories -> fetchNewsStoriesDetail(topStories?.getString(0)) }
            .thenAccept { newsStoriesDetail ->
                newsStoriesDetail?.let {
                    runOnUiThread {
                        jsonView.bindJson(it)
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        viewData()
    }
}