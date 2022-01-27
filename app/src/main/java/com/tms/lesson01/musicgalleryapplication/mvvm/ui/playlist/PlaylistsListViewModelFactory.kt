package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.IRecommendedPlaylistDao
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.IYourFavouritesPlaylistDao
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.INetworkMusicService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.artistAPIService.IArtistAPIService

/**
 * 1.2. Настройка View Model в соответствии с принципом обеспечения зависимостей:
 * Создаём фабрику. В конструктор передаём те же параметры, что и у View Model
 */
@Suppress("UNCHECKED_CAST")
class PlaylistsListViewModelFactory(
    private val musicModel: INetworkMusicService, // Наш экземпляр класса Model для обращения к ней. Тип переменной - наш интерфейс. NetworkMusicServiceModel() будет возвращать нам альбомы
    private var preferences: IAppSharedPreferences, // Объект preferences для обращения к SharedPreferences
    private var yourFavouritesPlaylistDao: IYourFavouritesPlaylistDao, // Объекты для обращения к Dao
    private var recommendedPlaylistDao: IRecommendedPlaylistDao,
    private var artistService: IArtistAPIService // Объекты для обращения к сервисам
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        (PlaylistsListViewModel(musicModel, preferences, yourFavouritesPlaylistDao, recommendedPlaylistDao, artistService) as T)
}