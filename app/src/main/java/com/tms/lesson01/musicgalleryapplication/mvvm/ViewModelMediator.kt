package com.tms.lesson01.musicgalleryapplication.mvvm

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelMediator: ViewModel() {
    // Переменные для передачи сообщения
    val isLoginSuccessLiveData = MutableLiveData<Unit>()
    val isLoginFailureLiveData = MutableLiveData<Unit>()
    // Переменные, которые будут отвечать за отображение прогресса (кружок). Т.е. события, на которые можно подписаться и слушать
    val showProgressLiveData = MutableLiveData<Unit>()
    val hideProgressLiveData = MutableLiveData<Unit>()

    // Инициализируем dataModel
    private val dataModel = DataModel()

    // Ф-ция, вызываемая по клику на кнопку в MainActivityView
    fun onSignUpClicked(nameText: String, emailText: String, passwordText: String, confirmPasswordText: String) {
        showProgressLiveData.postValue(Unit)

        Handler(Looper.getMainLooper()).postDelayed({
            val isSuccess = dataModel.onSignUpClicked(nameText, emailText, passwordText, confirmPasswordText)

            if (isSuccess) {
                isLoginSuccessLiveData.postValue(Unit)
            } else {
                isLoginFailureLiveData.postValue(Unit)
            }

            hideProgressLiveData.postValue(Unit)
        }, 3000)
    }

}