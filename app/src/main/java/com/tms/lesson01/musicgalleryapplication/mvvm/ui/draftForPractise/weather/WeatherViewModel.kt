package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.weather

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather.WeatherResponse
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather.WeatherNetwork
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.test.RxObservables
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

/**
 * Нам необходим сервис, который будет сообщать погоду в зависимости от нашей локации.
 * Мы уже создавали WeatherNetwork, доработаем его и будем использовать здесь.
 */
class WeatherViewModel : ViewModel() {
    companion object {
        private const val TAG = "WeatherViewModel"
    }

    private var count = 0
    private val weatherResultList = mutableListOf<WeatherResponse>()
    private val weatherNetwork = WeatherNetwork.getInstance()
    private val disposable = CompositeDisposable()

    // Live data
    var weatherLiveData = MutableLiveData<WeatherResponse>() // CURRENT LOCATION FROM FRAGMENT -> Используем, когда получаем погоду по текущей локации, передаваемой из фрагмента
    var weatherListLiveData = MutableLiveData<MutableList<WeatherResponse>>() // LOCATION FROM RxObservables.getLocationFlowableDataSource()
    var counterLiveData = MutableLiveData<TimeValue>() // For test RxObservables

    // 3.1. ОБРАБОТКА КЛИКА -> Создаём этот метод во view model
    fun onItemClicked(weatherResponseOnClick: WeatherResponse) {
//        TODO: make coroutine or RxJava request
        Log.i(TAG, weatherResponseOnClick.toString())
    }

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
                .subscribe({ weatherResponse ->
                    // Кладём результат в LiveData
                    weatherLiveData.postValue(weatherResponse) // CURRENT LOCATION FROM FRAGMENT -> Используем, когда получаем погоду по текущей локации, передаваемой из фрагмента
                }, { error ->
                    Log.e(TAG, error.message ?: "")
                })
        )
    }

    // 1.2. FOR TEST RX OBSERVABLES -> Подписываемся во view model на источник данных RxObservables.getFlowable() и записываем результат в counterLiveData
    fun testSubscribeOnFlowable() {
        disposable.add(
            RxObservables.getIntFlowableDataSource()
                // FOR TEST RX OBSERVABLES -> Преобразуем полученные из RxObservables.getFlowable() данные (Int) в другой тип (TimeValue) с помощью .map
                .map { intData -> TimeValue(operationCount = intData + 1, date = Date())}
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

    // 2.1. FOR TEST RX OBSERVABLES -> Подписываемся во view model на Observable (источник данных - локаций) и записываем результат в LiveData
    fun testSubscribeOnLocationFlowable() {
        disposable.add(
            RxObservables.getLocationFlowableDataSource()
                // FOR TEST RX OBSERVABLES -> Преобразуем полученные из RxObservables.getLocationFlowableDataSource() данные (Flowable<Location>) в другой тип (WeatherResponce, обернутый в observable) с помощью .concatMap
                // .concatMap применится и к RxObservables.getLocationFlowableDataSource(), и к нашему результату. Т.обр. мы получим и обработаем локацию несколько раз
                .concatMap { locationData -> getWeatherByLocation(location = locationData) } // LOCATION FROM RxObservables.getLocationFlowableDataSource()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d(TAG, "WEATHER COUNT: ${++count}, city: ${result.name}")
                    // Если конкретно этого элемента в списке нет- тогда обновляем список
                    if (!weatherResultList.contains(result)) {
                        weatherResultList.add(result)
                        weatherListLiveData.postValue(weatherResultList)
                    }
                }, { error ->
                    Log.e(TAG, error.message ?: "")
                })
        )
    }

    // Каждый раз, когда локация залетает, нам нужно делать запрос погоды по локации
    // Опишем это в отдельном приватном методе (где возвращаем ответ на запрос погоды по локации)
    private fun getWeatherByLocation(location: Location): Flowable<WeatherResponse> {
        return weatherNetwork.getWeatherServiceRx()
            .getWeatherByLocation(lat = location.latitude, lon = location.latitude).toFlowable()
    }
    // Если мы будем возвращать не Flowable, то наш WeatherResponse не успеет инициализироваться. Потому что инициализация происходит в другом потоке.
    // В таком случае, поток инициализации вернул бы результат уже после завершения потока исполнения и мы бы получили null.

    // Все запросы в RxJava нужно закрывать с помощью CompositeDisposable()
    override fun onCleared() {
        if (!disposable.isDisposed) {
            disposable.dispose()
            disposable.clear()
        }
        super.onCleared()
    }
}

// Класс для тестирования оператора .map
data class TimeValue(val operationCount: Int, val date: Date)