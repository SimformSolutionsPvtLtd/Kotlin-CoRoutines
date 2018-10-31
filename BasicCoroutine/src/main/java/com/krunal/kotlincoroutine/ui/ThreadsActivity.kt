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

class ThreadsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ThreadsActivity"

        fun newInstance(context: Context): Intent {
            return Intent(context, ThreadsActivity::class.java)
        }
    }

    val jsonView: JsonViewLayout by lazy {
        findViewById<JsonViewLayout>(R.id.jsonView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)
    }

    private fun fetchTopStories(): JSONArray? {
        var jsonArray: JSONArray? = null

        thread(true) {
            try {
                val response =
                    AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.TOP_STORIES}")

                response?.let {
                    jsonArray = JSONArray(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchTopStories Exception : $e")
            }
        }

        return jsonArray
    }

    private fun fetchNewsStoriesDetail(storyId: String?): JSONObject? {
        var jsonObject: JSONObject? = null
        thread(true) {
            try {
                if (storyId != null) {
                    val response =
                        AppConstanse.getApiMethod("${AppConstanse.API_BASE_URL}${AppConstanse.getNewsStoryDetail(storyId)}")

                    response?.let {
                        jsonObject = JSONObject(it)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "FetchNewsStoriesDetail Exception : $e")
            }
        }
        return jsonObject
    }

    private fun viewData() {
        val topStories = fetchTopStories()
        val newsStoriesDetail = fetchNewsStoriesDetail(topStories?.getString(0))
        val strResult = "{\n\"Response\" : \"$newsStoriesDetail\"\n}"
        jsonView.bindJson(strResult)
    }

    override fun onResume() {
        super.onResume()
        viewData()
    }
}