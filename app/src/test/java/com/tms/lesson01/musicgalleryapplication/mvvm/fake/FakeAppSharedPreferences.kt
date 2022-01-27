package com.tms.lesson01.musicgalleryapplication.mvvm.fake

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences

class FakeAppSharedPreferences: IAppSharedPreferences {
    override fun isRememberLoginAndPasswordSelected(): Boolean {
        return true
    }

    override fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean) {}

    override fun saveEmail(email: String) {}

    override fun savePassword(password: String) {}

    override fun getEmail(): String = ""

    override fun getPassword(): String =""

    override fun saveToken(token: String) {}

    override fun getToken(): String = ""
}