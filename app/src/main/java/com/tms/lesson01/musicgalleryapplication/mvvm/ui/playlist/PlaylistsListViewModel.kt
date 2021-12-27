package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist

import android.util.Log
import androidx.lifecycle.*
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.INetworkMusicService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.NetworkMusicServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.Playlist
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.IPlaylistDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PlaylistsListViewModel: ViewModel(), LifecycleEventObserver {
    // Константы
    companion object {
        private const val TAG = "PlaylistsListViewModel"
    }

    // Наш экземпляр класса Model для обращения к ней. Тип переменной - наш интерфейс. NetworkMusicServiceModel() будет возвращать нам альбомы
    private val musicModel: INetworkMusicService = NetworkMusicServiceModel()
    // Объект preferences для обращения к SharedPreferences
    private var preferences: IAppSharedPreferences? = null
    // Объект preferences для обращения к Dao
    private var playlistDao: IPlaylistDao? = null

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
    fun setPlaylistDao(playlistDao: IPlaylistDao) {
        this.playlistDao = playlistDao
    }

    fun logOut() {
        preferences?.saveToken("")
        logOutLiveData.value = Unit // поставили флажок
    }

//    private fun getCountries() {
//        val countries = model.getCountries()
//        countriesLiveData.value = countries
//    }

    // Получать плейлисты будем в корутине
    private fun getYourFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlistsList = musicModel.getYourFavorites() // Получаем список плейлистов1

                // Здесь закешируем наши плейлисты
                playlistDao?.insertPlaylists(*playlistsList.toTypedArray())

                yourFavoritesLiveData.postValue(playlistsList) // Передаём список в LiveData, чтобы передать в activity
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
            }

        }
    }

    // Получать плейлисты будем в корутине
    private fun getRecommendedPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistsList = musicModel.getRecommendedPlaylists() // Получаем список плейлистов2

            // Здесь закешируем наши плейлисты
            playlistDao?.insertPlaylists(*playlistsList.toTypedArray())

            recommendedPlaylistsLiveData.postValue(playlistsList) // Передаём список в LiveData, чтобы передать в activity
        }
    }
}