package com.krunal.coroutinesvsrx.data

import com.krunal.coroutinesvsrx.data.model.Launch
import com.krunal.coroutinesvsrx.data.model.RocketDetail
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface CoroutineRestfulAPI {
    @GET("launches/next")
    fun getNextLaunch(): Deferred<Launch>

    @GET("rockets/{rocketId}")
    fun getRocket(@Path("rocketId") rocketId: String): Deferred<RocketDetail>
}