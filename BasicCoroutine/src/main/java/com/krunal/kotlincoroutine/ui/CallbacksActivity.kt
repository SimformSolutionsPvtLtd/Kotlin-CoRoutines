package com.krunal.kotlincoroutine.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dandan.jsonhandleview.library.JsonViewLayout
import com.krunal.kotlincoroutine.R
import com.krunal.kotlincoroutine.utils.AppConstanse
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class CallbacksActivity : AppCompatActivity() {
    companion object {
        const val TAG = "CallbacksActivity"

        fun newInstance(context: Context): Intent {
            return Intent(context, CallbacksActivity::class.java)
        }
    }

    val jsonView: JsonViewLayout by lazy {
        findViewById<JsonViewLayout>(R.id.jsonView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)
    }

    private fun fetchTopStories(callback : (JSONArray?) -> Unit) {
        thread(true) {
            try {
                val response =
                    AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.TOP_STORIES}")

                if (response != null) {
                    callback.invoke(JSONArray(response))
                } else {
                    callback.invoke(null)
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchTopStories Exception : $e")
                callback.invoke(null)
            }
        }
    }

    private fun fetchNewsStoriesDetail(storyId: String?, callback : (JSONObject?) -> Unit) {
        thread(true) {
            try {
                if (storyId != null) {
                    val response =
                        AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.getNewsStoryDetail(storyId)}")

                    if (response != null) {
                        callback.invoke(JSONObject(response))
                    } else {
                        callback.invoke(null)
                    }
                } else {
                    callback.invoke(null)
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchNewsStoriesDetail Exception : $e")
                callback.invoke(null)
            }
        }
    }

    private fun viewData() {
        fetchTopStories { topStories ->
            fetchNewsStoriesDetail(topStories?.getString(0)) { newsStoriesDetail ->
                runOnUiThread {
                    jsonView.bindJson(newsStoriesDetail)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewData()
    }
}