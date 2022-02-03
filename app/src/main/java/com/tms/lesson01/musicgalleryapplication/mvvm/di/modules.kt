package com.tms.lesson01.musicgalleryapplication.mvvm.di

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.LocalStorageModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.AppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.AppDatabase
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.IRecommendedPlaylistDao
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.IYourFavouritesPlaylistDao
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.INetworkLoginService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.NetworkLoginServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.INetworkMusicService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.NetworkMusicServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.artistAPIService.IArtistAPIService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.lastFM.LastFMNetwork
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.LoginViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainSignUp.SignUpViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.PlaylistsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * 1. Здесь мы будем создавать модули, ответственные за создание экземпляров классов, которые нам нужно будет каду-то внедрять
 * Т.е. модули - это инструкции, как создавать объекты, которые мы будем внедрять.
 *
 * 2. Далее эти модули необходимо зарегистрировать в application классе, который, в свою очередь, необходимо зарегистрировать в манифесте
 */

// 1.1. Декларируем, как создавать те экземпляры классов, в которых нуждаются другие (инициализируем зависимости)
val appModule = module {
    // Пишем, как мы бы инициализировали эти объекты (н-р, в конструкторе view model, когда инициализируем её во фрагменте)
    single<INetworkLoginService> { NetworkLoginServiceModel() }
    single<IUserStorage> { LocalStorageModel() }
    single<IAppSharedPreferences> { AppSharedPreferences(androidContext()) }

    single<INetworkMusicService> { NetworkMusicServiceModel() }
    // Объекты для обращения к Dao
    single<IYourFavouritesPlaylistDao> { AppDatabase.getInstance(androidContext()).getYourFavouritesPlaylistDao() }
    single<IRecommendedPlaylistDao> { AppDatabase.getInstance(androidContext()).getRecommendedPlaylistDao() }
    // Объекты для обращения к сервисам
    // Наш artistService: IArtistAPIService
    single<IArtistAPIService> { LastFMNetwork.initService(IArtistAPIService::class.java) } // В статический метод передаём класс (Ретрофит сервис интерфейс), на основе которого мы должны построить сервис
}

// 1.2. Декларируем, как нам обеспечивать другие сущности (инициализируем сущности, которые зависят от 1.1.)
val viewModelModule = module {
    // Для view model есть отдельная настройка - viewModel{}
    viewModel { LoginViewModel(get(), get(), get()) } // Декларируем создание loginViewModel
    viewModel { SignUpViewModel(get(), get(), get()) } // Декларируем создание signUpViewModel
    viewModel { PlaylistsListViewModel(get(), get(), get(), get(), get()) }
}