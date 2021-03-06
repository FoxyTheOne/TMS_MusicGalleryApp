package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.lastFM

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.artistAPIService.IArtistAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Внутри этой реализации будут все возможные сервисы, которые будут относиться к запросам из нашего ресурса (last.fm)
 * и внутри него будет определенный интерфейс доступа к этому сервису и всем другим, которые мы будем в будущем использовать
 *
 * Класс LastFMNetwork  - синглтон
 *
 * Создаём класс LastFMNetwork на основе класса ArtistAPIService, но не расширяем его
 * ArtistAPIService - это наш интерфейс, где мы настраивали end point
 */
class LastFMNetwork private constructor() : ILastFMNetwork {
    private lateinit var artistService: IArtistAPIService

    companion object {
        private const val BASE_URL = "https://ws.audioscrobbler.com/"

        private var instance: LastFMNetwork? = null

        fun getInstance(): LastFMNetwork {
            if (instance == null) {
                instance = LastFMNetwork()
            }
            return instance!!
        }
    }

    // Метод, к-рый выполнится при инициализации:
    init {
        initService()
    }

    override fun getArtistService(): IArtistAPIService = artistService

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
            .addConverterFactory(GsonConverterFactory.create()) // С помощью чего мы будем преобразовывать json в объекты
            .client(client)
            .build()

        // 1.3. RETROFIT -> Чтобы инициализировать интерфейс, обращаться к нему и вызывать определенные методы, мы должны обратиться к созданному ретрофиту,
        // вызвать метод create и передать туда класс, на основе которого мы должны построить сервис
        artistService = retrofit.create(IArtistAPIService::class.java)
    }
}