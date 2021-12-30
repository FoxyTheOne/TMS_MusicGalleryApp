package com.tms.lesson01.musicgalleryapplication.mvvm.ui.music

import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IMusicStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.INetworkMusicService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.LocalStorageModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.NetworkMusicServiceModel

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для обеспечения UI данными (достать и доставить данные, не обрабатывать!) имеем отдельный класс
 * hw02. 5. DIP - Принцип инверсии зависимости. Зависимости внутри системы строятся на основе абстракций (private val localStorageModel: IUserStorage = LocalStorageModel())
 */
class MusicViewModel : ViewModel() {
    private val networkMusicServiceModel: INetworkMusicService = NetworkMusicServiceModel() // Инициализируем networkMusicServiceModel
    private val localStorageModel: IMusicStorage = LocalStorageModel() // Инициализируем localStorageModel для кэширования в случае успешного входа
}