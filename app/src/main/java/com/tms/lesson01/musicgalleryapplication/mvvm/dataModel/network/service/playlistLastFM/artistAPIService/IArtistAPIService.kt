package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.artistAPIService

import ArtistAPIResponce
import retrofit2.http.GET
import retrofit2.http.Query

interface IArtistAPIService {
    // https://ws.audioscrobbler.com/
    // 2.0/?method=chart.gettopartists&api_key=a35699f435445486aec22d7a19e652bf&format=json

    // Дефолтные значения сделаем статическими
    companion object {
        private const val BASE_PATH = "2.0"
        private const val KEY_METHOD = "chart.gettopartists"
        private const val KEY_API_KEY = "a35699f435445486aec22d7a19e652bf"
        private const val KEY_FORMAT = "json"
    }

    // Настроим end point. Т.к. вызываем ф-цию из корутина, пишем ключевое слово suspend
    @GET("$BASE_PATH/")
    suspend fun getTopArtists(
        @Query("method") method: String = KEY_METHOD,
        @Query("api_key") apiKey: String = KEY_API_KEY,
        @Query("format") format: String = KEY_FORMAT
    ): ArtistAPIResponce
}