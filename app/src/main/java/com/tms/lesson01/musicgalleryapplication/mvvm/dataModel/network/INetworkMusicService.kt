package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network

/**
 * hw02. 3. ISP - Принцип разделения интерфейса. Разделяем интерфейсы на более мелкие, специфические
 */
interface INetworkMusicService {
    fun getFavouriteMusic(): List<Any>
    fun updateFavouriteMusic(data: Any)
}