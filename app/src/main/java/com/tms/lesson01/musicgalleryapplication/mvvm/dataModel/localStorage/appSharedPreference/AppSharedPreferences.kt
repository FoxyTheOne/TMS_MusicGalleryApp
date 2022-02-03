package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference

import android.content.Context
import android.content.SharedPreferences

/**
 * Этот класс - Синглтон
 * Однако, обеспечим мы это с помощью Koin (single {...})
 */
class AppSharedPreferences (context: Context): IAppSharedPreferences {
    // SharedPreference сохраняет данные в виде "ключ-значение". Значит, нам нужно создать ключ:
    companion object{
        // Константа - имя файла
        private const val PREFERENCE_NAME = "AppSharedPreference"
        // Ключ для отслеживания, проставлена галочка или нет
        private const val PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED = "PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED"
        // Ключи для данных
        private const val PREFERENCE_EMAIL = "PREFERENCE_EMAIL"
        private const val PREFERENCE_PASSWORD = "PREFERENCE_PASSWORD"
        private const val PREFERENCE_TOKEN = "PREFERENCE_TOKEN"
    }

    // Наш preference:
    private val preferences: SharedPreferences = context.getSharedPreferences( // У нас будет один общий файл, поэтому .getSharedPreferences
        PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )

    // Выбрана ли галочка "оставаться в системе", т.е. сохранить данные (выбирали ли эту галочку ранее)
    // edit() нам уже не нужен, т.к. мы не будем ничего изменять
    // false в нашем случае - это дефолтное значение. Т.е. если ранее ничего не выбрано, возвратится false
    override fun isRememberLoginAndPasswordSelected(): Boolean {
        return preferences.getBoolean(
            PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED,
            false
        )
    }

    // Каждый раз, когда пользователь кликает по галочке, сюда передаётся значение
    override fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean) {
        preferences.edit()?.putBoolean(
            PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED,
            isSelected
        )?.apply()
    }

    override fun saveEmail(email: String) {
        preferences.edit()?.putString(
            PREFERENCE_EMAIL,
            email
        )?.apply()
    }

    override fun savePassword(password: String) {
        preferences.edit()?.putString(
            PREFERENCE_PASSWORD,
            password
        )?.apply()
    }

    override fun getEmail(): String {
        return preferences.getString(
            PREFERENCE_EMAIL,
            ""
        ) ?: ""
    }

    override fun getPassword(): String {
        return preferences.getString(
            PREFERENCE_PASSWORD,
            ""
        ) ?: ""
    }

    override fun saveToken(token: String) {
        preferences.edit()?.putString(
            PREFERENCE_TOKEN,
            token
        )?.apply()
    }

    override fun getToken(): String {
        return preferences.getString(
            PREFERENCE_TOKEN,
            ""
        ) ?: ""
    }
}