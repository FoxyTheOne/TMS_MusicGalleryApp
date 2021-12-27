package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.Playlist

/**
 * hw02. 3. ISP - Принцип разделения интерфейса. Разделяем интерфейсы на более мелкие, специфические
 */
interface INetworkMusicService {
    fun getFavouriteMusic(): List<Any>

    fun getYourFavorites(): List<Playlist>
    fun getRecommendedPlaylists(): List<Playlist>

    fun updateFavouriteMusic(data: Any)
}