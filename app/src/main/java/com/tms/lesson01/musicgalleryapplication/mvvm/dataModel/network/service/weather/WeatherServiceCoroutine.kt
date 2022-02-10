package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather

import com.tms.lesson01.musicgalleryapplication.BuildConfig
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Код из проекта преподавателя (для виджета погоды)
 * 1. Интерфейс, в котором описываем запросы, которые планируем делать
 */
interface WeatherServiceCoroutine {
    companion object {
        private const val BASE_PATH = "data/2.5"
        private const val WEATHER_PATH = "weather"
        private const val KEY_CITY = "Minsk"
        private const val KEY_UNITS = "metric"
        private const val KEY_API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY
    }

    @GET("$BASE_PATH/{weather}")
    suspend fun getWeather(
        @Path("weather") weather: String = WEATHER_PATH,
        @Query("q") city: String = KEY_CITY,
        @Query("units") units: String = KEY_UNITS,
        @Query("appid") appid: String = KEY_API_KEY,
    ): WeatherResponse
}
