package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather

import com.tms.lesson01.musicgalleryapplication.BuildConfig
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Код из проекта преподавателя (для виджета погоды)
 * 1. Интерфейс, в котором описываем запросы, которые планируем делать
 */
interface WeatherServiceRx {
    companion object {
        private const val BASE_PATH = "data/2.5" // base path
        private const val WEATHER_PATH = "weather" // path
        private const val KEY_UNITS = "metric"
        private const val KEY_API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY
    }

    @GET("$BASE_PATH/{weather}") // base path
    // Это не корутин, поэтому suspend не пишем
    fun getWeatherByLocation(
        @Path("weather") weather: String = WEATHER_PATH, // path
        // query parameters
        // required
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = KEY_API_KEY,
        // optional
        @Query("units") units: String = KEY_UNITS,
    ): Single<WeatherResponse> // Выбираем observable. Поставим Single, т.л. нам надо 1 раз сделать запрос и отобразить его

// api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
}
