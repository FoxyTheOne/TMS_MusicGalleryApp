package com.tms.lesson01.musicgalleryapplication.mvvm.utils.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast

/**
 * Создадим Bound Service
 * BOUND_SERVICE -> 1. Для начала, задекларируем Bound Service в Manifest
 * BOUND_SERVICE -> 4. Расширяем Service(), а так же наш интерфейс IAppBinder
 */
class AppBoundService: Service(), IAppBinder {

    // BOUND_SERVICE -> 5. Когда кто-то подписывается на сервис, он в первую очередь залетает в этот метод, где он должен получить байндер. Вернём его:
    override fun onBind(intent: Intent?): IBinder {
        return AppBoundServiceBinder()
    }

    override fun showToast() {
        Toast.makeText(
            this, "Toast from AppBoundService showed", Toast.LENGTH_LONG
        ).show()
    }

    override fun goToForeground() {
        TODO("Not yet implemented")
    }

    override fun goToBound() {
        TODO("Not yet implemented")
    }

    // BOUND_SERVICE -> 3. Создадим вложенный класс-байндер
    inner class AppBoundServiceBinder : Binder() {
        // BOUND_SERVICE -> 6. Здесь мы должны написать код, чтобы байндер возвратил интерфейс
        fun getAppBoundService(): IAppBinder = this@AppBoundService
    }

}

// BOUND_SERVICE -> 2. Создаём интерфейс для общения с сервисом. В нём прописываем необходимые методы в дальнейшем
interface IAppBinder{
    fun showToast()

    fun goToForeground()
    fun goToBound()
}

// BOUND_SERVICE -> 7. Теперь перейдём в наш фрагмент NotificationFragment и подпишемся на сервис