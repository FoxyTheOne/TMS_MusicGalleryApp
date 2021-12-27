package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.Playlist

/**
 * 3. Абстрактный класс Database, который методом build строит базу данных
 */
// Внутри entities = [] мы говорим, какие наши таблицы будет в себе объединять наша база данных
@Database(entities = [Playlist::class], version = 1)
abstract class AppDatabaseAbstract: RoomDatabase() {
    companion object{
        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabaseAbstract::class.java, "AppDatabase"
        ).build()
    }

    // Метод, который будет возвращать интерфейс Dao, для взаимодействия с базой данных:
    abstract fun getPlaylistDao(): IPlaylistDao
}