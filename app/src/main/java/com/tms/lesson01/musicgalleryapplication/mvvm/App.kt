package com.tms.lesson01.musicgalleryapplication.mvvm

import android.app.Application
import com.tms.lesson01.musicgalleryapplication.mvvm.di.appModule
import com.tms.lesson01.musicgalleryapplication.mvvm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * По дефолту в Android используется свой Application класс.
 * Если же мы хотим внедрить в Application классе какую-то логику, которую нам нужно делать именно там, то мы можем расширить его
 *
 * Этот класс обязательно нужно зарегистрировать в манифесте (<application
 * android:name=".mvvm.App")
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.ERROR)
            androidContext(applicationContext)
            modules(listOf(appModule, viewModelModule))
        }
    }
}