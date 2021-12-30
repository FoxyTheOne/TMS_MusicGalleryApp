package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.lastFM

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.artistAPIService.IArtistAPIService
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

    // Создаём ретрофит сервис на основе ранее созданного сервиса
    private fun initService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create()) // С помощью чего мы будем преобразовывать json в объекты
            .build()

        // Чтобы инициализировать интерфейс, обращаться к нему и вызывать определенные методы, мы должны обратиться к созданному ретрофиту, вызвать метод create
        // и передать туда класс, на основе которого мы должны построить сервис
        artistService = retrofit.create(IArtistAPIService::class.java)
    }
}