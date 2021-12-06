package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.INetworkLoginService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.LocalStorageModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.NetworkLoginServiceModel

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для обеспечения UI данными (достать и доставить данные, не обрабатывать!) имеем отдельный класс
 * hw02. 5. DIP - Принцип инверсии зависимости. Зависимости внутри системы строятся на основе абстракций (private val localStorageModel: IUserStorage = LocalStorageModel())
 */
class LoginViewModel: ViewModel() {
    // Переменные для передачи сообщения
    val isLoginSuccessLiveData = MutableLiveData<Unit>()
    val isLoginFailureLiveData = MutableLiveData<Unit>()
    // Переменные, которые будут отвечать за отображение прогресса (кружок). Т.е. события, на которые можно подписаться и слушать
    val showProgressLiveData = MutableLiveData<Unit>()
    val hideProgressLiveData = MutableLiveData<Unit>()
    // Перемнные для сохранения информации во время пересоздания Activity из-за поворота экрана:
    val nameLiveData = MutableLiveData<String>()
    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val confirmPasswordLiveData = MutableLiveData<String>()

    // LoginViewModel зависим от следующих инициализируемых сущностей. Всё, что он делает, делает благодаря им
    private val networkLoginServiceModel: INetworkLoginService = NetworkLoginServiceModel() // Инициализируем networkLoginModel
    private val localStorageModel: IUserStorage = LocalStorageModel() // Инициализируем localStorageModel для кэширования в случае успешного входа

    // Ф-ция, вызываемая по клику на кнопку в MainActivityView
    fun onSignUpClicked(nameText: String, emailText: String, passwordText: String, confirmPasswordText: String) {
        showProgressLiveData.postValue(Unit) // Сообщаем нашему view (LoginActivityView), что нужно показать прогресс

        // Функция с задержкой, для тестового проекта. В реальном проекте задержка (Handler) не нужна
        Handler(Looper.getMainLooper()).postDelayed({
            val successToken = networkLoginServiceModel.onSignUpClicked(nameText, emailText, passwordText, confirmPasswordText)

            hideProgressLiveData.postValue(Unit) // Сообщаем нашему view (LoginActivityView), что нужно спрятать прогресс
            if (successToken != null) {
                localStorageModel.saveTokenToLocalStorage(token = successToken) // кэшируем токен, чтобы можно было использовать его по всему приложению в дальнейшем
                isLoginSuccessLiveData.postValue(Unit) // Если у нас есть токен, значит вход успешный
            } else {
                isLoginFailureLiveData.postValue(Unit)
            }
        }, 3000)
    }

}