package com.tms.lesson01.musicgalleryapplication.mvvm.fake

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage

class FakeLocalStorageModel: IUserStorage {
    override fun saveTokenToLocalStorage(token: String) {}
}