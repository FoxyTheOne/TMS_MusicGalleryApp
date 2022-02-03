package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.INetworkLoginService

/**
 * 1.1. Настройка View Model в соответствии с принципом обеспечения зависимостей:
 * Если внутри View Model используются объекты, то их нужно передавать через конструктор.
 * Переделаем наш код, учитывая этот факт
 */
class LoginViewModel(
    // LoginViewModel зависим от следующих инициализируемых сущностей. Всё, что он делает, делает благодаря им
    // Объявляем их в конструкторе View Model (т.е. здесь), инициализируем - с помощью Koin (di -> modules, viewModelModule)
    private val networkLoginServiceModel: INetworkLoginService,
    private val localStorageModel: IUserStorage, // Для кэширования в случае успешного входа
    private val preferences: IAppSharedPreferences
): ViewModel() {

    // Переменные для передачи сообщения
    val isLoginSuccessLiveData = MutableLiveData<Boolean>()
    val isLoginFailureLiveData = MutableLiveData<Boolean>()
    // Переменные, которые будут отвечать за отображение прогресса (кружок). Т.е. события, на которые можно подписаться и слушать
    val showProgressLiveData = MutableLiveData<Boolean>()
    val hideProgressLiveData = MutableLiveData<Boolean>()
    // Перемнные для сохранения информации во время пересоздания Activity из-за поворота экрана:
    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val checkBoxRememberLoginAndPasswordLiveData = MutableLiveData<Boolean>()

    // Каждый раз, когда мы кликаеи, будет исполняться этот метод. Здесь мы сохраняем статус check box
    fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean) {
        preferences.setRememberLoginAndPasswordSelectedOrNot(isSelected)
    }

    // Ф-ция, вызываемая по клику на кнопку в MainActivityView
    fun onLoginClicked(emailText: String, passwordText: String) {
        showProgressLiveData.postValue(true) // Сообщаем нашему view (LoginActivityView), что нужно показать прогресс

//        // Функция с задержкой, для тестового проекта. В реальном проекте задержка (Handler) не нужна
//        Handler(Looper.getMainLooper()).postDelayed({
            val successToken = networkLoginServiceModel.onLoginClicked(emailText, passwordText)

            hideProgressLiveData.postValue(true) // Сообщаем нашему view (LoginActivityView), что нужно спрятать прогресс
            if (successToken != null) {
//                localStorageModel.saveTokenToLocalStorage(token = successToken) // кэшируем токен, чтобы можно было использовать его по всему приложению в дальнейшем
//                Пока уберем эту строку
                saveToken(successToken = successToken)
                saveLoginData(emailText, passwordText)
                isLoginSuccessLiveData.postValue(true) // Если у нас есть токен, значит вход успешный
            } else {
                isLoginFailureLiveData.postValue(true)
            }
//        }, 3000)
    }

    fun getStoredData() {
        preferences.let {
            if (it.isRememberLoginAndPasswordSelected()) {
                emailLiveData.postValue(it.getEmail())
                passwordLiveData.postValue(it.getPassword())
                // Т.е. вытягиваем сохраненные значения из local storage и кладем их в LiveData
                checkBoxRememberLoginAndPasswordLiveData.postValue(true) // кладем true
            }
        }
    }

    fun setUpdatedEmail(email: String) {
        if (email != emailLiveData.value) {
            emailLiveData.value = email
        }
    }

    fun setUpdatedPassword(password: String) {
        if (password != passwordLiveData.value) {
            passwordLiveData.value = password
        }
    }

    private fun saveToken(successToken: String) {
        // Токен сохраняем в любом случае, при успешном входе
        preferences.saveToken(successToken)
    }

    private fun saveLoginData(emailText: String, passwordText: String) {
        // Если выбрано сохранить, то при успешном входе, сохраняем
        preferences.let {
            if (it.isRememberLoginAndPasswordSelected()) {
                it.saveEmail(emailText)
                it.savePassword(passwordText)
            }
        }
    }
}