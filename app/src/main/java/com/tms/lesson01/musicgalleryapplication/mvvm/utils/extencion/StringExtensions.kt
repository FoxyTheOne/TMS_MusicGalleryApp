package com.tms.lesson01.musicgalleryapplication.mvvm.utils.extencion

import androidx.core.util.PatternsCompat

// Здесь размещаем статические методы для String
// Проверка email
fun String?.isEmailValid(): Boolean {
    return if (this != null && this.isNotBlank()) {
        PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()
    } else {
        false
    }
}

// Проверка password
fun String?.isPasswordValid(): Boolean {
    return if (this != null && this.isNotBlank()) {
        this.length > 5
    } else {
        false
    }
}