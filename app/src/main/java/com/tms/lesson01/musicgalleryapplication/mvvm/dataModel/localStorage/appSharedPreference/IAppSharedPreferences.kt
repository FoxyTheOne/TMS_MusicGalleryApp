package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference

interface IAppSharedPreferences {
    // Выбрана ли галочка "оставаться в системе", т.е. сохранить данные
    fun isRememberLoginAndPasswordSelected(): Boolean

    // Каждый раз, когда пользователь кликает по галочке, сюда передаётся значение
    fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean)

    // Методы для сохранения данных при успешном входе, если выбрана галочка
    fun saveEmail(email: String)
    fun savePassword(password: String)

    fun getEmail(): String
    fun getPassword(): String

    fun saveToken(token: String)
    fun getToken(): String
}