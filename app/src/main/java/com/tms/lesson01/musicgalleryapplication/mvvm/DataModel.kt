package com.tms.lesson01.musicgalleryapplication.mvvm

/**
 * В Модели написать метод для проверки name: просто проверить что он не пустой
 * В Модели написать метод для проверки email: проверить что это действительно email (это мы уже делали)
 * В Модели написать методы для проверки password: минимум 5 символов + проверить что мы ввели пароль в двох полях одинаковый
 */
class DataModel {
    fun onSignUpClicked(name: String, email:String, password: String, confirmPassword: String): Boolean {
        val nameValid = name.isNotBlank()
        val emailValid = email.isNotBlank()&&android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordValid = password.isNotBlank()&&password.length > 5
        val confirmPasswordValid = password.equals(confirmPassword)

        return nameValid&&emailValid&&passwordValid&&confirmPasswordValid
    }
}