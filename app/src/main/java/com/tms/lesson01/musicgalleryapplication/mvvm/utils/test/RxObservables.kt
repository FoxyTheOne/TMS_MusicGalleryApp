package com.tms.lesson01.musicgalleryapplication.mvvm.utils.test

import android.os.Handler
import android.os.Looper
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

/**
 * Класс со статическими методами для тестирования RxJava
 */
class RxObservables {
    companion object {

        // Может выпускать большое к-во данных и имеет BackpressureStrategy
        fun getFlowable(): Flowable<Int> {
            return Flowable.create({ flowableEmitter ->
                repeat(10_000_000) { countInt ->
                    flowableEmitter.onNext(countInt)
                }
            }, BackpressureStrategy.LATEST)
        }
    }

    // Либо возвращает результат, либо ошибку
    fun getCompletable(): Completable {
        return Completable.create { completableEmitter ->
            try {
                //  TODO do some work
                Handler(Looper.myLooper()!!).postDelayed({
                    completableEmitter.onComplete() // Если всё хорошо
                }, 4000)
            } catch (e: Exception) {
                completableEmitter.onError(e) // Если ошибка
            }
        }
    }

    // Либо возвращает значение, либо нет, либо ошибку
    fun getMaybe(): Maybe<String> {
        return Maybe.create { maybeEmitter ->
            try {
                //  TODO do some work
                Handler(Looper.myLooper()!!).postDelayed({
                    maybeEmitter.onComplete()
                    maybeEmitter.onSuccess("Success")
                }, 4000)
            } catch (e: Exception) {
                maybeEmitter.onError(e)
            }
        }
    }

}
