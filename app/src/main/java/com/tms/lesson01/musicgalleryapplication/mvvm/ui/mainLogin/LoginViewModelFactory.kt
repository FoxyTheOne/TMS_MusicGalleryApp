package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.INetworkLoginService

/**
 * 1.2. Настройка View Model в соответствии с принципом обеспечения зависимостей:
 * Создаём фабрику. В конструктор передаём те же параметры, что и у View Model
 */
@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val networkLoginServiceModel: INetworkLoginService,
    private val localStorageModel: IUserStorage,
    private val preferences: IAppSharedPreferences
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        (LoginViewModel(networkLoginServiceModel, localStorageModel, preferences) as T)
}