package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase

import android.content.Context

/**
 * 4. Класс, который бедт возвращать тот или иной Dao. Синглтон.
 * Т.е. не конкретно базу данных. Это та прослойка, которая будет возвращать только Dao и только в те места, где нам это нужно.
 * Внутри этого класса: 1) создаётся база данных, 2) этот класс регулирует доступ к Dao уровням, но не к базе данных.
 */
class AppDatabase private constructor() : IYourFavouritesPlaylistDatabase, IRecommendedPlaylistDatabase {
    companion object {
        private var instance: AppDatabase? = null
        // Наш интерфейс Dao, для взаимодействия с базой данных
        private var yourFavouritesPlaylistDao: IYourFavouritesPlaylistDao? = null
        private var recommendedPlaylistDao: IRecommendedPlaylistDao? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = AppDatabase()
                yourFavouritesPlaylistDao = AppDatabaseAbstract.buildDatabase(context).getYourFavouritesPlaylistDao()
                recommendedPlaylistDao = AppDatabaseAbstract.buildDatabase(context).getRecommendedPlaylistDao()
            }
            return instance!!
        }
    }

    override fun getYourFavouritesPlaylistDao(): IYourFavouritesPlaylistDao = yourFavouritesPlaylistDao!!
    override fun getRecommendedPlaylistDao(): IRecommendedPlaylistDao = recommendedPlaylistDao!!
}