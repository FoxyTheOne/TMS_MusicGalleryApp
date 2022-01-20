package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.artist.Artist
import android.util.Log
import androidx.lifecycle.*
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.INetworkMusicService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.NetworkMusicServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.YourFavouritesPlaylist
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.IRecommendedPlaylistDao
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.IYourFavouritesPlaylistDao
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.RecommendedPlaylist
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.artistAPIService.IArtistAPIService
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.atomic.AtomicReferenceArray

class PlaylistsListViewModel: ViewModel(), LifecycleEventObserver {
    // Константы
    companion object {
        private const val TAG = "PlaylistsListViewModel"
    }

    // Наш экземпляр класса Model для обращения к ней. Тип переменной - наш интерфейс. NetworkMusicServiceModel() будет возвращать нам альбомы
    private val musicModel: INetworkMusicService = NetworkMusicServiceModel()
    // Объект preferences для обращения к SharedPreferences
    private var preferences: IAppSharedPreferences? = null
    // Объекты для обращения к Dao
    private var yourFavouritesPlaylistDao: IYourFavouritesPlaylistDao? = null
    private var recommendedPlaylistDao: IRecommendedPlaylistDao? = null
    // Объекты для обращения к сервисам
    private var artistService: IArtistAPIService? = null

//    // Создаём LiveData, которое слушает Activity:
//    val countriesLiveData = MutableLiveData<List<String>>()
//    // Наш экземпляр класса Model для обращения к ней. Тип переменной - наш интерфейс:
//    val model: ICountriesModel = CountriesModel()

    // 4. Создаём LiveData, которое будет слушать наш Activity, чтобы подтягивать к себе список плейлистов (List<>):
    val yourFavoritesLiveData = MutableLiveData<List<YourFavouritesPlaylist>>()
    val recommendedPlaylistsLiveData = MutableLiveData<List<RecommendedPlaylist>>()

    val artistsLiveData = MutableLiveData<List<Artist>>()
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
                getArtists()
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
    fun setYourFavouritesPlaylistDao(yourFavouritesPlaylistDao: IYourFavouritesPlaylistDao) {
        this.yourFavouritesPlaylistDao = yourFavouritesPlaylistDao
    }
    fun setRecommendedPlaylistDao(recommendedPlaylistDao: IRecommendedPlaylistDao) {
        this.recommendedPlaylistDao = recommendedPlaylistDao
    }
    fun setArtistService(service: IArtistAPIService) {
        this.artistService = service
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
                val yourFavouritesPlaylistsList = musicModel.getYourFavorites() // Получаем список плейлистов1

                // Здесь закешируем наши плейлисты
                yourFavouritesPlaylistDao?.insertYourFavouritesPlaylists(*yourFavouritesPlaylistsList.toTypedArray())

                yourFavoritesLiveData.postValue(yourFavouritesPlaylistsList) // Передаём список в LiveData, чтобы передать в activity
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
            }
        }
        println("ON_CREATE getYourFavorites")
    }

    // Получать плейлисты будем в корутине
    private fun getRecommendedPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recommendedPlaylistsList = musicModel.getRecommendedPlaylists() // Получаем список плейлистов2

                // Здесь закешируем наши плейлисты
                recommendedPlaylistDao?.insertRecommendedPlaylists(*recommendedPlaylistsList.toTypedArray())

                recommendedPlaylistsLiveData.postValue(recommendedPlaylistsList) // Передаём список в LiveData, чтобы передать в activity
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
            }
        }
        println("ON_CREATE getRecommendedPlaylists")
    }

//    private fun getArtists() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val artistsList = artistService?.getTopArtists()?.artists?.artist ?: listOf()// Получаем список плейлистов2. Если он null - вернуть пустой список
//
//                // TODO Здесь закешируем наши плейлисты
//
//                Log.i(TAG, "ArtistList: $artistsList")
//                artistsLiveData.postValue(artistsList) // Передаём список в LiveData, чтобы передать в activity
//                Log.i(TAG, "Список передан в LiveData")
//            } catch (e: Exception) {
//                Log.e(TAG, e.message ?: "")
//            }
//        }
//        println("ON_CREATE getArtists вызвана")
//    }

    private fun getArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val artistsList = artistService?.getTopArtists()?.artists?.artist ?: listOf()// Получаем список плейлистов2. Если он null - вернуть пустой список

                while (artistsList.isEmpty()){
                    val artistsList = artistService?.getTopArtists()?.artists?.artist
                    Log.i(TAG, "ArtistList: $artistsList")
                    if (artistsList != null) {
                        artistsLiveData.postValue(artistsList!!)
                        Log.i(TAG, "Список передан в LiveData")
                        break
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
            }
        }
        println("ON_CREATE getArtists вызвана")
    }

}