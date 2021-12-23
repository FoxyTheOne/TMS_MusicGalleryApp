package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference

import android.content.Context
import android.content.SharedPreferences

/**
 * Этот класс - Синглтон. В котлин такой объект создаётся просто (всместо class пишем object).
 * Однако, чтобы понять логику, создадим его, для начала, самостоятельно:
 * 1. Создаём статический метод, который возвращает один и тот же экземпляр класса. Т.е. создаёт объект только если его ещё не создавали
 * 2. Конструктор нашего класса должен быть приватным
 */
class AppSharedPreferences private constructor(): IAppSharedPreferences {
    // 1. SharedPreference сохраняет данные в виде "ключ-значение". Значит, нам нужно создать ключ:
    companion object{
        // Константа - имя файла
        private const val PREFERENCE_NAME = "AppSharedPreference"
        // Ключ для отслеживания, проставлена галочка или нет
        private const val PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED = "PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED"
        // Ключи для данных
        private const val PREFERENCE_EMAIL = "PREFERENCE_EMAIL"
        private const val PREFERENCE_PASSWORD = "PREFERENCE_PASSWORD"
        private const val PREFERENCE_TOKEN = "PREFERENCE_TOKEN"

        // 2. Объект, который мы будем возвращать. Тип - наш интерфейс
        private var instance: IAppSharedPreferences? = null
        // Инициализируем SharedPreferences. Когда мы инициализируем instance, сразу же нужно инициализировать и preferences
        private var preferences: SharedPreferences? = null

        // 3. Создаём статический метод, который возвращает один и тот же экземпляр класса. Т.е. создаёт объект только если его ещё не создавали
        fun getInstance(context: Context): IAppSharedPreferences {
            if (instance == null) {
                instance = AppSharedPreferences() // Создаём экземпляр класса + инициализируем preferences
                preferences = context.getSharedPreferences( // У нас будет один общий файл, поэтому .getSharedPreferences
                    PREFERENCE_NAME,
                    Context.MODE_PRIVATE)
            }
            return instance!! // Если уже был создан, вернуть его, не создавая новый
        }
    }

    // Выбрана ли галочка "оставаться в системе", т.е. сохранить данные (выбирали ли эту галочку ранее)
    // edit() нам уже не нужен, т.к. мы не будем ничего изменять
    // false в нашем случае - это дефолтное значение. Т.е. если ранее ничего не выбрано, возвратится false
    override fun isRememberLoginAndPasswordSelected(): Boolean {
        return preferences?.getBoolean(
            PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED,
            false
        ) ?: false
    }

    // Каждый раз, когда пользователь кликает по галочке, сюда передаётся значение
    override fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean) {
        preferences?.edit()?.putBoolean(
            PREFERENCE_IS_REMEMBER_LOGIN_AND_PASSWORD_SELECTED,
            isSelected
        )?.apply()
    }

    override fun saveEmail(email: String) {
        preferences?.edit()?.putString(
            PREFERENCE_EMAIL,
            email
        )?.apply()
    }

    override fun savePassword(password: String) {
        preferences?.edit()?.putString(
            PREFERENCE_PASSWORD,
            password
        )?.apply()
    }

    override fun getEmail(): String {
        return preferences?.getString(
            PREFERENCE_EMAIL,
            ""
        ) ?: ""
    }

    override fun getPassword(): String {
        return preferences?.getString(
            PREFERENCE_PASSWORD,
            ""
        ) ?: ""
    }

    override fun saveToken(token: String) {
        preferences?.edit()?.putString(
            PREFERENCE_TOKEN,
            token
        )?.apply()
    }

    override fun getToken(): String {
        return preferences?.getString(
            PREFERENCE_TOKEN,
            ""
        ) ?: ""
    }
}