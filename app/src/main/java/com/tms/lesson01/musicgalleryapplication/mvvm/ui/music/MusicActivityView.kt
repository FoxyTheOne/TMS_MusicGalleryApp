package com.tms.lesson01.musicgalleryapplication.mvvm.ui.music

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tms.lesson01.musicgalleryapplication.R

/**
 * hw02. Создаём новый экран MusicActivity
 * hw02. 1. SRP - Принцип единственной ответственности. Для обновления UI имеем отдельный класс
 */
class MusicActivityView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
    }
}