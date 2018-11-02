package com.krunal.kotlincoroutine.utils

import kotlinx.coroutines.Dispatchers
import java.util.concurrent.*
import kotlin.coroutines.*

fun <T> future(context: CoroutineContext = Dispatchers.Default, block: suspend () -> T): CompletableFuture<T> =
    CompletableFutureCoroutine<T>(context).also { block.startCoroutine(completion = it) }

class CompletableFutureCoroutine<T>(override val context: CoroutineContext) : CompletableFuture<T>(), Continuation<T> {
    override fun resumeWith(result: Result<T>) {
        result
            .onSuccess { complete(it) }
            .onFailure { completeExceptionally(it) }
    }
}