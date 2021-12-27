package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainSignUp.acticityOld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.tms.lesson01.musicgalleryapplication.R
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.activityOld.CountriesActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainSignUp.SignUpViewModel

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для обновления UI имеем отдельный класс
 */
class SignUpActivityView : AppCompatActivity() {
    // Переменные класса
    private lateinit var viewModel: SignUpViewModel
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressCircular: ProgressBar
    private lateinit var nameField: TextInputLayout
    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var confirmPasswordField: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_sign_up)
        // Инициализируем viewModel, чтобы работать с ViewModel (согласно примеру с сайта)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        // Оглашаем наши локальные переменные
        val buttonSignUp: AppCompatButton = findViewById(R.id.buttonSignUp)
        // Переменные для отображения прогресса
        frameLayout = findViewById(R.id.frameLayout)
        progressCircular = findViewById(R.id.progressCircular)
        // Переменные для полей ввода
        nameField = findViewById(R.id.nameField)
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        confirmPasswordField = findViewById(R.id.confirmPasswordField)

//        // Восстанавливаем значения при повороте экрана:
//        restoreValues()

        // Сохраняем введенные в поля значения для последнующего восстановления при необходимости:
        nameField.editText?.addTextChangedListener(){
            viewModel.nameLiveData.value = it.toString()
        }
        emailField.editText?.addTextChangedListener(){
            viewModel.emailLiveData.value = it.toString()
        }
        passwordField.editText?.addTextChangedListener(){
            viewModel.passwordLiveData.value = it.toString()
        }
        confirmPasswordField.editText?.addTextChangedListener(){
            viewModel.confirmPasswordLiveData.value = it.toString()
        }

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
        viewModel.isSignUpSuccessLiveData.observe(this, {
            val intent = Intent(this, CountriesActivity::class.java)
            startActivity(intent)
        })
        viewModel.isSignUpFailureLiveData.observe(this, {
            Toast.makeText(this, "Something wrong with your data. Please, try again!", Toast.LENGTH_LONG).show()
        })
        viewModel.showProgressLiveData.observe(this, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(this, {
            hideProgress()
        })
    }

//    private fun restoreValues() {
//        nameField.editText?.setText(viewModel.nameLiveData.value ?: "")
//        emailField.editText?.setText(viewModel.emailLiveData.value ?: "")
//        passwordField.editText?.setText(viewModel.passwordLiveData.value ?: "")
//        confirmPasswordField.editText?.setText(viewModel.confirmPasswordLiveData.value ?: "")
//    }

    private fun showProgress() {
        frameLayout.isVisible = true
        progressCircular.isVisible = true
    }

    private fun hideProgress() {
        frameLayout.isVisible = false
        progressCircular.isVisible = false
    }
}