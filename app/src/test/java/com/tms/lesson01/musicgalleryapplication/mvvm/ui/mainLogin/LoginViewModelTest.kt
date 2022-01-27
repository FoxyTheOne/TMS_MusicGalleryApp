package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.INetworkLoginService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.NetworkLoginServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.fake.FakeAppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.fake.FakeLocalStorageModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var loginViewModel: LoginViewModel

    // Для начала, создадим ViewModel по конструктору
    @Before
    fun prepareLoginViewModel() {
        loginViewModel = LoginViewModel(
            NetworkLoginServiceModel() as INetworkLoginService,
            FakeLocalStorageModel(),
            FakeAppSharedPreferences()
        )
    }

    // Далее тестируем метод onLoginClicked из нашей View Model
    @Test
    fun checkLogin_ifPasswordWrong() {
        // Передаём для теста правильный логин и не верный парооль
        loginViewModel.onLoginClicked("qqqw@gmail.com", "123")

        // И проверяем, как отработает LiveData. Узнаём, какое получим значение и далее сравним его с верным
        val isLoginSuccessLiveData = loginViewModel.isLoginSuccessLiveData.value
        val isLoginFailureLiveData = loginViewModel.isLoginFailureLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailureLiveData)
    }

    @Test
    fun checkLogin_ifEmailWrong() {
        // Передаём для теста не правильный логин и верный парооль
        loginViewModel.onLoginClicked("qqqw.com", "123456")

        // И проверяем, как отработает LiveData. Узнаём, какое получим значение и далее сравним его с верным
        val isLoginSuccessLiveData = loginViewModel.isLoginSuccessLiveData.value
        val isLoginFailureLiveData = loginViewModel.isLoginFailureLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailureLiveData)
    }

    @Test
    fun checkLogin_ifPasswordEmpty() {
        loginViewModel.onLoginClicked("qqqw@gmail.com", "")
        val isLoginSuccessLiveData = loginViewModel.isLoginSuccessLiveData.value
        val isLoginFailureLiveData = loginViewModel.isLoginFailureLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailureLiveData)
    }

    @Test
    fun checkLogin_ifEmailWrongEmpty() {
        loginViewModel.onLoginClicked("", "123456")
        val isLoginSuccessLiveData = loginViewModel.isLoginSuccessLiveData.value
        val isLoginFailureLiveData = loginViewModel.isLoginFailureLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailureLiveData)
    }

    @Test
    fun checkLogin_ifEmailAndPasswordCorrect() {
        loginViewModel.onLoginClicked("qqqw@gmail.com", "123456")
        val isLoginSuccessLiveData = loginViewModel.isLoginSuccessLiveData.value
        val isLoginFailureLiveData = loginViewModel.isLoginFailureLiveData.value

        assertEquals(true, isLoginSuccessLiveData)
        assertEquals(null, isLoginFailureLiveData)
    }
}