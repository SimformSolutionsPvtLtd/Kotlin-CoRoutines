package com.krunal.kotlincoroutine.utils

import java.net.URL

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
        return URL(url).readText()
    }
}