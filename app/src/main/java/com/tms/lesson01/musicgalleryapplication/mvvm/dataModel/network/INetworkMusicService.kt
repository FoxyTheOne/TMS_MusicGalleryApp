package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.RecommendedPlaylist
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.YourFavouritesPlaylist

/**
 * hw02. 3. ISP - Принцип разделения интерфейса. Разделяем интерфейсы на более мелкие, специфические
 */
interface INetworkMusicService {
    fun getFavouriteMusic(): List<Any>

    fun getYourFavorites(): List<YourFavouritesPlaylist>
    fun getRecommendedPlaylists(): List<RecommendedPlaylist>

    fun updateFavouriteMusic(data: Any)
}