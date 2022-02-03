package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Код из проекта преподавателя (для виджета погоды)
 * 2. Класс WeatherNetwork - Синглтон
 *
 * Создаём класс WeatherNetwork на основе класса WeatherService, но не расширяем его
 * WeatherServiceCoroutine - это наш интерфейс, где мы настраивали end point
 *
 * Добавляем WeatherServiceRx - это наш интерфейс2, где мы настраивали end point
 */
class WeatherNetwork private constructor() : IWeatherNetwork {

    private lateinit var weatherServiceCoroutine: WeatherServiceCoroutine
    private lateinit var weatherServiceRx: WeatherServiceRx

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"

        private var instance: IWeatherNetwork? = null

        fun getInstance(): IWeatherNetwork {
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

    override fun getWeatherServiceCoroutine(): WeatherServiceCoroutine = weatherServiceCoroutine
    override fun getWeatherServiceRx(): WeatherServiceRx = weatherServiceRx

    // 1.1. RETROFIT -> Напишем метод для создания ретрофита
    private fun initService() {
        // 2.1. INTERCEPTOR -> Создаём экземпляр(ы) класса (инициализируем Interceptor)
        val bodyInterceptor = HttpLoggingInterceptor()
        val headersInterceptor = HttpLoggingInterceptor()
        // 2.2. INTERCEPTOR -> Определяем уровень, что будет выводить Interceptor (что мы отправляем и что получаем)
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        headersInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        // 2.3. INTERCEPTOR -> билдер для Interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(bodyInterceptor)
            .addInterceptor(headersInterceptor)
            .build()

        initWeatherServiceCoroutine(client)
        initWeatherServiceRx(client)
    }

    private fun initWeatherServiceCoroutine(client: OkHttpClient) {
        // 1.2. RETROFIT -> Создаём ретрофит сервис на основе ранее созданного сервиса (билдер)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // 1.3. RETROFIT -> Чтобы инициализировать интерфейс, обращаться к нему и вызывать определенные методы, мы должны обратиться к созданному ретрофиту,
        // вызвать метод create и передать туда класс, на основе которого мы должны построить сервис
        weatherServiceCoroutine = retrofit.create(WeatherServiceCoroutine::class.java)
    }

    private fun initWeatherServiceRx(client: OkHttpClient) {
        // В RxJava используется другой CallAdapter, поэтому билдеры напишем отдельно, в разных функциях
        // (допишем его в билдер)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()

        weatherServiceRx = retrofit.create(WeatherServiceRx::class.java)
    }

}
