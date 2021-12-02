package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage

/**
 * hw02. 3. ISP - Принцип разделения интерфейса. Разделяем интерфейсы на более мелкие, специфические
 */
interface IMusicStorage {
    fun saveFavoriteMusicToLocalStorage(token: String)
}