package com.krunal.kotlincoroutine.utils

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object AppConstanse {
    const val TAG = "AppConstanse"
    const val API_BASE_URL = "https://hacker-news.firebaseio.com/v0/"
    const val TOP_STORIES = "topstories.json?print=pretty"
    //const val NEWS_STORY_DETAIL = "item/{storyId}.json?print=pretty"
    const val IMAGE_URL_1 = "http://www.freeimageslive.com/galleries/objects/general/pics/woodenbox0482.jpg"
    const val IMAGE_URL_2 = "http://i.imgur.com/DvpvklR.png"

    fun getNewsStoryDetail(storyId: String): String {
        return "item/$storyId.json?print=pretty"
    }

    fun getApiMethod(url: String): String? {
        val apiUrl = URL(url)
        val conn = apiUrl.openConnection() as HttpURLConnection
        conn.readTimeout = 15000 //15 second
        conn.connectTimeout = 15000 //15 second
        conn.requestMethod = "GET"

        val responseCode = conn.responseCode
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))
            val sb = StringBuffer("")

            do {
                val line = bufferedReader.readLine()
                line?.let {
                    sb.append(line)
                }
            } while (line != null)

            bufferedReader.close()
            Log.e(TAG, "GET True : $sb")
            return sb.toString()
        } else {
            Log.e(TAG, "GET False : $responseCode")
        }
        return null
    }
}