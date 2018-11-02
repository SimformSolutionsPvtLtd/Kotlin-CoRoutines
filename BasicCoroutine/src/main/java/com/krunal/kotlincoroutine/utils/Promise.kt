package com.krunal.kotlincoroutine.utils

import java.lang.ref.WeakReference
import java.util.concurrent.Future

typealias Promise<T> = Future<T>

fun <T, R> T.doAsyncResultPromise(
    exceptionHandler: ((Throwable) -> Unit)? = crashLogger,
    task: AsyncContext<T>.() -> R
): Promise<R> {
    val context = AsyncContext(WeakReference(this))
    return BackgroundExecutor.submit {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr)
            throw thr
        }
    }
}

fun <T, Y> Promise<T>.thenCompose(handler: (T) -> Promise<Y>): Promise<Y> = doAsyncResultPromise {
    val res = this@thenCompose.get()
    handler.invoke(res).get()
}

fun <T, Y> Promise<T>.thenAccept(handler: (T) -> Y): Promise<Y> = doAsyncResultPromise {
    val res = this@thenAccept.get()
    handler.invoke(res)
}