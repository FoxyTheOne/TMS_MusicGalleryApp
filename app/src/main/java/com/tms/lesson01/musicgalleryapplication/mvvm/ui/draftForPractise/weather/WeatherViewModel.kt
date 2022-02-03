package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.weather

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather.WeatherResponse
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather.WeatherNetwork
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.test.RxObservables
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Нам необходим сервис, который будет сообщать погоду в зависимости от нашей локации.
 * Мы уже создавали WeatherNetwork, доработаем его и будем использовать здесь.
 */
class WeatherViewModel : ViewModel() {
    companion object {
        private const val TAG = "WeatherViewModel"
    }

    private val weatherNetwork = WeatherNetwork.getInstance()
    private val disposable = CompositeDisposable()

    // Live data
    var weatherLiveData = MutableLiveData<WeatherResponse>()
    var counterLiveData = MutableLiveData<Int>() // For test RxObservables

    // Погоду мы будем запрашивать по локации
    fun getWeather(location: Location) {
        // В каждый запрос RxJava мы добавляем disposable.add
        disposable.add(
            // Получаем сервис
            weatherNetwork.getWeatherServiceRx()
                // Вызываем метод сервиса (end point)
                // Локацию будем передавать из фрагмента
                .getWeatherByLocation(lat = location.latitude, lon = location.latitude)
                // Далее, от нашего observable, который мы получили из .getWeatherByLocation(), вызовем цепочку методов для получения результата
                .subscribeOn(Schedulers.io()) // Подписка - поток, в котором будет выполняться код (подписались в бэкграунде)
                .observeOn(AndroidSchedulers.mainThread()) // Прослушка - поток, в кором будем получать результат кода
                .subscribe({ weatherResponce ->
                    // Кладём результат в LiveData
                    weatherLiveData.postValue(weatherResponce)
                }, { error ->
                    Log.e(TAG, error.message ?: "")
                })
        )
    }

    // For test RxObservables
    fun testFlowable() {
        disposable.add(
            RxObservables.getFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ result ->
                    counterLiveData.postValue(result)
                    Log.d(TAG, result.toString())
                }, { error ->
                    Log.e(TAG, error.message ?: "")
                })
        )
    }

    // Все запросы в RxJava нужно закрывать с помощью CompositeDisposable()
    override fun onCleared() {
        if (!disposable.isDisposed) {
            disposable.dispose()
            disposable.clear()
        }
        super.onCleared()
    }
}