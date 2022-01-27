package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainSignUp

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.INetworkLoginService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.LocalStorageModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.NetworkLoginServiceModel

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для обеспечения UI данными (достать и доставить данные, не обрабатывать!) имеем отдельный класс
 * hw02. 5. DIP - Принцип инверсии зависимости. Зависимости внутри системы строятся на основе абстракций (private val localStorageModel: IUserStorage = LocalStorageModel())
 *
 * 1.1. Настройка View Model в соответствии с принципом обеспечения зависимостей:
 * Если внутри View Model используются объекты, то их нужно передавать через конструктор.
 * Переделаем наш код, учитывая этот факт
 */
class SignUpViewModel(
    // SignUpViewModel зависим от следующих инициализируемых сущностей. Всё, что он делает, делает благодаря им
    // Объявляем их в конструкторе View Model (т.е. здесь), инициализируем - в конструкторе фрагмента (SignUpFragment)
    private val networkLoginServiceModel: INetworkLoginService,
    private val localStorageModel: IUserStorage, // Для кэширования в случае успешного входа
    private val preferences: IAppSharedPreferences
): ViewModel() {

    // Переменные для передачи сообщения
    val isSignUpSuccessLiveData = MutableLiveData<Unit>()
    val isSignUpFailureLiveData = MutableLiveData<Unit>()
    // Переменные, которые будут отвечать за отображение прогресса (кружок). Т.е. события, на которые можно подписаться и слушать
    val showProgressLiveData = MutableLiveData<Unit>()
    val hideProgressLiveData = MutableLiveData<Unit>()
    // Перемнные для сохранения информации во время пересоздания Activity из-за поворота экрана:
    val nameLiveData = MutableLiveData<String>()
    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val confirmPasswordLiveData = MutableLiveData<String>()
    val checkBoxRememberLoginAndPasswordLiveData = MutableLiveData<Boolean>()

//    !!! Этот код (инициализация сущностей, создание объектов) переносим в конструктор View Model:
//    // SignUpViewModel зависим от следующих инициализируемых сущностей. Всё, что он делает, делает благодаря им
//    private val networkLoginServiceModel: INetworkLoginService = NetworkLoginServiceModel() // Инициализируем networkLoginModel
//    private val localStorageModel: IUserStorage = LocalStorageModel() // Инициализируем localStorageModel для кэширования в случае успешного входа

//    //    private val preferences = AppSharedPreferences.getInstance()
//    private var preferences: IAppSharedPreferences? = null
//    //    Здесь нам не хватает Dependency injection, который будем изучать позже. Пока что создадим метод, к-рый будет отправлять нам SharedPreferences из фрагмента
//    fun setSharedPreferences(preferences: IAppSharedPreferences) {
//        this.preferences = preferences
//    }

    // Каждый раз, когда мы кликаем, будет исполняться этот метод. Здесь мы сохраняем статус check box
    fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean) {
        preferences.setRememberLoginAndPasswordSelectedOrNot(isSelected)
    }

    // Ф-ция, вызываемая по клику на кнопку в MainActivityView
    fun onSignUpClicked(nameText: String, emailText: String, passwordText: String, confirmPasswordText: String) {
        showProgressLiveData.postValue(Unit) // Сообщаем нашему view (LoginActivityView), что нужно показать прогресс

        // Функция с задержкой, для тестового проекта. В реальном проекте задержка (Handler) не нужна
        Handler(Looper.getMainLooper()).postDelayed({
            val successToken = networkLoginServiceModel.onSignUpClicked(nameText, emailText, passwordText, confirmPasswordText)

            hideProgressLiveData.postValue(Unit) // Сообщаем нашему view (LoginActivityView), что нужно спрятать прогресс
            if (successToken != null) {
//                localStorageModel.saveTokenToLocalStorage(token = successToken) // кэшируем токен, чтобы можно было использовать его по всему приложению в дальнейшем
//                Пока уберем эту строку
                saveToken(successToken = successToken)
                saveLoginData(emailText, passwordText)
                isSignUpSuccessLiveData.postValue(Unit) // Если у нас есть токен, значит вход успешный
            } else {
                isSignUpFailureLiveData.postValue(Unit)
            }
        }, 3000)
    }

    fun getStoredData() {
        preferences.let {
            if (it.isRememberLoginAndPasswordSelected()) {
                checkBoxRememberLoginAndPasswordLiveData.postValue(true) // кладем true
            }
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