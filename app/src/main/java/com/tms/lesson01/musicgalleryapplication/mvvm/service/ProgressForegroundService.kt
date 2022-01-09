package com.tms.lesson01.musicgalleryapplication.mvvm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tms.lesson01.musicgalleryapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Создадим Foreground Service
 * FOREGROUND_SERVICE -> 1. Для начала, задекларируем Foreground Service в Manifest. А так же добавим туда разрешение для Foreground Service
 */
class ProgressForegroundService: Service() {
    companion object {
        private const val TAG = "ProgressForeground"
        private const val CHANNEL_ID = "foreground_notification_channel_id"
    }

    private var notificationBuilder: NotificationCompat.Builder? = null
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onCreate() {
        super.onCreate()

        // FOREGROUND_SERVICE -> 3. Вызываем метод для создания Channel
        // Добавляем проверку, т.к. создавать NotificationChannel можно только начиная с API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }

        // FOREGROUND_SERVICE -> 4. Channel создан, теперь можно приступить непосредственно к созданию уведомления
        // 4.1. Создаём notification c помощью билдера, который первым будет показываться нашему пользователю
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Progress notification")
            .setContentText("Current progress")
            // Для отображения прогресса добавляем .setProgress() (вместо pendingIntent, который мы использовали в NotificationFragment)
            .setProgress(100, 0, false)
            // Так же добавим приоритет
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // 4.2. Для того, чтобы Service из обычного перешел в Foreground, нам нужно вызвать метод startForeground() внутри этого сервиса
        startForeground(0, notificationBuilder?.build())

        // 4.3. И далее вызываем метод, который будет обновлять наш notification
        updateProgress()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        serviceJob.cancel()
    }

    // FOREGROUND_SERVICE -> 2. Создадим Channel
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        // 2.4. Доступаемся к NotificationManager
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 2.1. Находим наши строки
        val channelName1 = getString(R.string.foregroundNotification_name)
        val channelDescription1 = getString(R.string.foregroundNotification_description)
        // 2.2. Настраиваем приоритет
        val importance1 = NotificationManager.IMPORTANCE_DEFAULT

        // 2.3. Создаём Channel и регистрируем его
        val channel1 =  NotificationChannel(CHANNEL_ID, channelName1, importance1).apply {
            description = channelDescription1
        }

        // 2.5. И вызываем у него (NotificationManager) метод createNotificationChannel(), куда передаём наш channel
        notificationManager.createNotificationChannel(channel1)
    }

    // FOREGROUND_SERVICE -> 5. Обновляем наш notification
    private fun updateProgress() {
        // 5.1. Доступаемся к NotificationManager
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 5.2 Описываем обновление нашего notification
        serviceScope.launch(Dispatchers.IO) {
            var i = 0
            while (i < 100) {
                Thread.sleep(2000)
                i += 10
                Log.d(TAG, "updateProgress: $i")
                // Если notificationBuilder != null
                notificationBuilder?.let { builder ->
                    builder
                        .setContentText("Current progress: ${i}%")
                        .setProgress(100, i, false)
                    // 5.3. Передаём notificationManager наш билдер notification
                    notificationManager.notify(0, builder.build())
                }
            }
            // 5.4. Когда прогресс заканчивается, закрываем Foreground, удаляем уведомления, stop service
            stopForeground(true)
            notificationManager.cancelAll()
            stopSelf()
        }
    }

    // FOREGROUND_SERVICE -> 6. Запустим наш Foreground Service из NotificationFragment
}