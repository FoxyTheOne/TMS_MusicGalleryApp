package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Код из проекта преподавателя (для виджета погоды)
 * 2. Класс WeatherNetwork - Синглтон
 *
 * Создаём класс WeatherNetwork на основе класса WeatherService, но не расширяем его
 * WeatherService - это наш интерфейс, где мы настраивали end point
 */
class WeatherNetwork private constructor() : IWeatherNetwork {

    private lateinit var weatherService: WeatherService

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"

        private var instance: WeatherNetwork? = null

        fun getInstance(): WeatherNetwork {
            if (instance == null) {
                instance = WeatherNetwork()
            }

            return instance!!
        }
    }

    // Метод, к-рый выполнится при инициализации:
    init {
        initService()
    }

    override fun getWeatherService(): WeatherService = weatherService

    // 1.1. RETROFIT -> Напишем метод для создания ретрофита
    private fun initService() {
        // 2.1. INTERCEPTOR -> Создаём экземпляр(ы) класса (инициализируем Interceptor)
        val bodyInterceptor = HttpLoggingInterceptor()
        val headersInterceptor = HttpLoggingInterceptor()
        // 2.2. INTERCEPTOR -> Определяем уровень, что будет выводить Interceptor (что мы отправляем и что получаем)
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        headersInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        // 2.1. INTERCEPTOR -> билдер для Interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(bodyInterceptor)
            .addInterceptor(headersInterceptor)
            .build()

        // 1.2. RETROFIT -> Создаём ретрофит сервис на основе ранее созданного сервиса (билдер)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // 1.3. RETROFIT -> Чтобы инициализировать интерфейс, обращаться к нему и вызывать определенные методы, мы должны обратиться к созданному ретрофиту,
        // вызвать метод create и передать туда класс, на основе которого мы должны построить сервис
        weatherService = retrofit.create(WeatherService::class.java)
    }
}