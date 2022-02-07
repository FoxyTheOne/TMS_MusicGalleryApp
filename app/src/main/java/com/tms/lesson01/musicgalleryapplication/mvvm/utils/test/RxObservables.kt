package com.tms.lesson01.musicgalleryapplication.mvvm.utils.test

import android.location.Location
import android.os.Handler
import android.os.Looper
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

/**
 * Класс со статическими методами для тестирования RxJava
 *
 * Популярные операторы RXJava: .map, .flatMap, .concatMap
 */
class RxObservables {

    companion object {

        // 1.1. FOR TEST RX OBSERVABLES -> Создаём Observable (источник данных), на который можно подписаться (для тестирования оператора .map)
        // Может выпускать большое к-во данных и имеет BackpressureStrategy
        fun getIntFlowableDataSource(): Flowable<Int> {
            return Flowable.create({ flowableEmitter ->
                repeat(10_000_000) { countInt ->
                    flowableEmitter.onNext(countInt)
                }
            }, BackpressureStrategy.LATEST)
        }

        // 2.1. FOR TEST RX OBSERVABLES -> Создаём новый Observable (источник данных - локаций), на который можно подписаться (для тестирования оператора .concatMap)
        // Метод возвращает Flowable, чтобы на него снова можно было подписаться и получить следующую локацию
        fun getLocationFlowableDataSource(): Flowable<Location> {
            return Flowable.create({ flowableEmitterLocation ->
                flowableEmitterLocation.onNext(Location("").apply {
                    latitude = 50.483047
                    longitude = 30.475750
                })
                flowableEmitterLocation.onNext(Location("").apply {
                    latitude = 53.90195183331898
                    longitude = 27.54095118448498
                })
                flowableEmitterLocation.onNext(Location("").apply {
                    latitude = 28.62733953991019
                    longitude = -82.12573493124957
                })
                flowableEmitterLocation.onNext(Location("").apply {
                    latitude = 40.70651946986305
                    longitude = -73.93852065088775
                })
            }, BackpressureStrategy.BUFFER)
        }

        // Либо возвращает результат, либо ошибку
        fun getCompletableDataSource(): Completable {
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
        fun getMaybeDataSource(): Maybe<String> {
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

}
