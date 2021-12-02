package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.tms.lesson01.musicgalleryapplication.R
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.tms.lesson01.musicgalleryapplication.success.SuccessLoginActivity

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для обновления UI имеем отдельный класс
 */
class LoginActivityView : AppCompatActivity() {
    // Переменные класса
    private lateinit var viewModel: LoginViewModel
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressCircular: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Инициализируем viewModel, чтобы работать с ViewModel (согласно примеру с сайта)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Оглашаем наши локальные переменные
        val nameField: TextInputLayout = findViewById(R.id.nameField)
        val emailField: TextInputLayout = findViewById(R.id.emailField)
        val passwordField: TextInputLayout = findViewById(R.id.passwordField)
        val confirmPasswordField: TextInputLayout = findViewById(R.id.confirmPasswordField)
        val buttonSignUp: AppCompatButton = findViewById(R.id.buttonSignUp)
        // Переменные для отображения прогресса
        frameLayout = findViewById(R.id.frameLayout)
        progressCircular = findViewById(R.id.progressCircular)


        // Определяем действие по клику на кнопку
        buttonSignUp.setOnClickListener {
            val nameText = nameField.editText?.text.toString()
            val emailText = emailField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()
            val confirmPasswordText = confirmPasswordField.editText?.text.toString()

            viewModel.onSignUpClicked(nameText, emailText, passwordText, confirmPasswordText)
        }

        // Вызываем метод, чтобы подписаться на события
        subscribeOnLiveData()
    }

    /**
     * В случае успеха - перевести на страницу Success, как на занятии
     * В случае ошибки - вывести Toast, как на занятии
     */
    // Подвисываемся, чтобы слышать события из LiveData (в ViewModelMediator). Если какое-то из этих событий случится, будет вызван соответствующий метод, описанный в observe у этого события
    private fun subscribeOnLiveData() {
        viewModel.isLoginSuccessLiveData.observe(this, {
            val intent = Intent(this, SuccessLoginActivity::class.java)
            startActivity(intent)
        })
        viewModel.isLoginFailureLiveData.observe(this, {
            Toast.makeText(this, "Something wrong with your data. Please, try again!", Toast.LENGTH_LONG).show()
        })
        viewModel.showProgressLiveData.observe(this, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(this, {
            hideProgress()
        })
    }

    private fun showProgress() {
        frameLayout.isVisible = true
        progressCircular.isVisible = true
    }

    private fun hideProgress() {
        frameLayout.isVisible = false
        progressCircular.isVisible = false
    }
}