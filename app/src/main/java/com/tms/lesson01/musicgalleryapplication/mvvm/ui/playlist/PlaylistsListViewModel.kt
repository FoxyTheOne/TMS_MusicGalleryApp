package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist

import androidx.lifecycle.*
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.INetworkMusicService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.NetworkMusicServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.customObject.Playlist
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences

class PlaylistsListViewModel: ViewModel(), LifecycleEventObserver {
    // Наш экземпляр класса Model для обращения к ней. Тип переменной - наш интерфейс. NetworkMusicServiceModel() будет возвращать нам альбомы
    private val musicModel: INetworkMusicService = NetworkMusicServiceModel()
    // Объект preferences для обращения к SharedPreferences
    private var preferences: IAppSharedPreferences? = null

//    // Создаём LiveData, которое слушает Activity:
//    val countriesLiveData = MutableLiveData<List<String>>()
//    // Наш экземпляр класса Model для обращения к ней. Тип переменной - наш интерфейс:
//    val model: ICountriesModel = CountriesModel()

    // 4. Создаём LiveData, которое будет слушать наш Activity, чтобы подтягивать к себе список плейлистов (List<>):
    val yourFavoritesLiveData = MutableLiveData<List<Playlist>>()
    val recommendedPlaylistsLiveData = MutableLiveData<List<Playlist>>()
    // LiveData для выхода из приложения
    val logOutLiveData = MutableLiveData<Unit>()

    // В этом классе мы хотим слушать события, поэтому наследуемся от LifecycleEventObserver и override метод onStateChanged
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        // Жизненный цикл нашего activity:
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                println("ON_CREATE")
                getYourFavorites() // метод для вывода на экран наш список при создании activity
                getRecommendedPlaylists()
            }
            Lifecycle.Event.ON_START -> {
                println("ON_START")
            }
            Lifecycle.Event.ON_RESUME -> {
                println("ON_RESUME")
            }
            Lifecycle.Event.ON_PAUSE -> {
                println("ON_PAUSE")
            }
            Lifecycle.Event.ON_STOP -> {
                println("ON_STOP")
            }
            Lifecycle.Event.ON_DESTROY -> {
                println("ON_DESTROY")
            }
            Lifecycle.Event.ON_ANY -> {
                println("ON_ANY")
            }
        }
    }

    fun setSharedPreferences(preferences: IAppSharedPreferences) {
        this.preferences = preferences
    }

    fun logOut() {
        preferences?.saveToken("")
        logOutLiveData.value = Unit // поставили флажок
    }

//    private fun getCountries() {
//        val countries = model.getCountries()
//        countriesLiveData.value = countries
//    }

    private fun getYourFavorites() {
        val playlistsList = musicModel.getYourFavorites() // Получаем список плейлистов1
        yourFavoritesLiveData.value = playlistsList // Передаём список в LiveData, чтобы передать в activity
    }

    private fun getRecommendedPlaylists() {
        val playlistsList = musicModel.getRecommendedPlaylists() // Получаем список плейлистов2
        recommendedPlaylistsLiveData.value = playlistsList // Передаём список в LiveData, чтобы передать в activity
    }
}