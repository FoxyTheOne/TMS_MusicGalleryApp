package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.LocalStorageModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.INetworkLoginService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.NetworkLoginServiceModel

class LoginViewModel: ViewModel() {

    // Переменные для передачи сообщения
    val isLoginSuccessLiveData = MutableLiveData<Unit>()
    val isLoginFailureLiveData = MutableLiveData<Unit>()
    // Переменные, которые будут отвечать за отображение прогресса (кружок). Т.е. события, на которые можно подписаться и слушать
    val showProgressLiveData = MutableLiveData<Unit>()
    val hideProgressLiveData = MutableLiveData<Unit>()
    // Перемнные для сохранения информации во время пересоздания Activity из-за поворота экрана:
    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val checkBoxRememberLoginAndPasswordLiveData = MutableLiveData<Boolean>()

    // LoginViewModel зависим от следующих инициализируемых сущностей. Всё, что он делает, делает благодаря им
    private val networkLoginServiceModel: INetworkLoginService = NetworkLoginServiceModel() // Инициализируем networkLoginModel
    private val localStorageModel: IUserStorage = LocalStorageModel() // Инициализируем localStorageModel для кэширования в случае успешного входа

    //    private val preferences = AppSharedPreferences.getInstance()
    private var preferences: IAppSharedPreferences? = null
    //    Здесь нам не хватает Dependency injection, который будем изучать позже. Пока что создадим метод, к-рый будет отправлять нам SharedPreferences из фрагмента
    fun setSharedPreferences(preferences: IAppSharedPreferences) {
        this.preferences = preferences
    }

    // Каждый раз, когда мы кликаеи, будет исполняться этот метод. Здесь мы сохраняем статус check box
    fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean) {
        preferences?.setRememberLoginAndPasswordSelectedOrNot(isSelected)
    }

    // Ф-ция, вызываемая по клику на кнопку в MainActivityView
    fun onLoginClicked(emailText: String, passwordText: String) {
        showProgressLiveData.postValue(Unit) // Сообщаем нашему view (LoginActivityView), что нужно показать прогресс

        // Функция с задержкой, для тестового проекта. В реальном проекте задержка (Handler) не нужна
        Handler(Looper.getMainLooper()).postDelayed({
            val successToken = networkLoginServiceModel.onLoginClicked(emailText, passwordText)

            hideProgressLiveData.postValue(Unit) // Сообщаем нашему view (LoginActivityView), что нужно спрятать прогресс
            if (successToken != null) {
//                localStorageModel.saveTokenToLocalStorage(token = successToken) // кэшируем токен, чтобы можно было использовать его по всему приложению в дальнейшем
//                Пока уберем эту строку
                saveToken(successToken)
                saveLoginData(emailText, passwordText)
                isLoginSuccessLiveData.postValue(Unit) // Если у нас есть токен, значит вход успешный
            } else {
                isLoginFailureLiveData.postValue(Unit)
            }
        }, 3000)
    }

    fun getStoredData() {
        preferences?.let {
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
        preferences?.saveToken(successToken)
    }

    private fun saveLoginData(emailText: String, passwordText: String) {
        // Если выбрано сохранить, то при успешном входе, сохраняем
        preferences?.let {
            if (it.isRememberLoginAndPasswordSelected()) {
                it.saveEmail(emailText)
                it.savePassword(passwordText)
            }
        }
    }
}