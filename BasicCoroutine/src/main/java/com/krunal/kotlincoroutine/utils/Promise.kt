package com.krunal.kotlincoroutine.utils

import kotlinx.coroutines.*

typealias Promise<T> = Deferred<T>

fun <T, Y> Promise<T>.thenAccept(handler: (T) -> Y): Promise<Y> = GlobalScope.async {
    val res = this@thenAccept.await()
    handler.invoke(res)
}

fun <T, Y> Promise<T>.thenCompose(handler: (T) -> Promise<Y>): Promise<Y> = GlobalScope.async {
    val res = this@thenCompose.await()
    handler.invoke(res).await()
}

fun <T> async(block: suspend CoroutineScope.() -> T) : Promise<T> = async {
    block.invoke(this)
}

