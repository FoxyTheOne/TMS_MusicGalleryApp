package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.otherApplicationComponent.serviceAndNotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.service.AppBoundService
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.service.IAppBinder

class NotificationFragment: Fragment() {
    companion object {
        private const val TAG = "NotificationFragment"
        // К каждой группе NOTIFICATION мы создаём свой CHANNEL_ID
        private const val CHANNEL_ID = "notification_channel_id"
        private const val CHANNEL_ID_2 = "notification_channel_id2"
    }

    // BOUND_SERVICE -> 8. Создадим наш Service connection (второй параметр при запуске сервиса с помощью Intent)
    // 8.1. Создадим переменную, чтобы инициализировать её при создании Service connection
    private var iAppBinder: IAppBinder? = null

    // 8.2. Создадим экземпляр Service connection
    private val connection = object : ServiceConnection {
        // Когда мы забандимся к нашему сервису, вызовется метод onServiceConnected() и мы получим экземпляр binder: IBinder?
        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            // Проверяем binder на null. Если он не null, приводим к типу нашего байндера и вызываем наш метод, который вернет интерфейс сервиса IAppBinder и мы сможем вызывать его методы
            binder?.let {
                iAppBinder = (it as AppBoundService.AppBoundServiceBinder).getAppBoundService()
                // В этом месте мы можем заново привязаться, если переводили Bound service в Foreground при закрытии приложения (вызвав наш метод из интерфейса):
                // iAppBinder?.goToBound()
            }
        }

        // этот метод будет вызван, если связь с сервисом была прервана неожиданно
        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_draft_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BOUND_SERVICE -> 9. Найдем нашу кнопочку и повесим на неё .setOnClickListener
        view.findViewById<AppCompatButton>(R.id.button_showToast).setOnClickListener {
            iAppBinder?.showToast()
        }

        // NOTIFICATIONS -> 3. вызываем методы createChannels() и затем createNotifications()
        // Добавляем проверку, т.к. создавать NotificationChannel можно только начиная с API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
        createNotifications()

//        // !!! FOREGROUND_SERVICE -> 6. Запустим наш Foreground Service из NotificationFragment
//        requireContext().startService(Intent(requireContext(), ProgressForegroundService::class.java))
    }

    // BOUND_SERVICE -> 7. Подпишемся на сервис в нашем фрагменте NotificationFragment
    // Если мы подписываемся на Bound Service в каком-то методе жизненного цикла, мы обязательно должны просчитать точку входа и точку выхода (н-р, если мы входим в методе onStart, то в методе onStop должны отписаться)
    override fun onStart() {
        super.onStart()
        // 7.1. Запускаем сервис с помощью Intent:
        requireContext().bindService(Intent(requireContext(), AppBoundService::class.java), connection, Context.BIND_AUTO_CREATE)
        // BIND_AUTO_CREATE - каждый раз, когда мы бандимся, если сервис не был создан, он будет создаваться автоматически
        // Второй параметр - Service connection. Это объект, внутри которого мы будем получать наш AppServiceBinder (байндер). Здесь не достаточно просто создать экземпляр класса
    }

    override fun onStop() {
        super.onStop()
        // 7.2. Заканчиваем соединение. Сюда также передаём наш Service connection. Создадим его (см. выше)
        requireContext().unbindService(connection)
    }

    // NOTIFICATIONS -> 1. Для начала, создадим Channel
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        // 1.4. Доступаемся к NotificationManager
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 1.1. Находим наши строки
        val channelName1 = getString(R.string.notification_name)
        val channelDescription1 = getString(R.string.notification_description)
        // 1.2. Настраиваем приоритет
        val importance1 = NotificationManager.IMPORTANCE_HIGH

        // 1.3. Создаём Channel и регистрируем его
        val channel1 =  NotificationChannel(CHANNEL_ID, channelName1, importance1).apply {
            description = channelDescription1
        }

        // 1.5. И вызываем у него (NotificationManager) метод createNotificationChannel(), куда передаём наш channel
        notificationManager.createNotificationChannel(channel1)

        // NOTIFICATIONS -> Создадим второе уведомление:
        val channelName2 = getString(R.string.notification_name2)
        val channelDescription2 = getString(R.string.notification_description2)
        val importance2 = NotificationManager.IMPORTANCE_HIGH

        val channel2 =  NotificationChannel(CHANNEL_ID_2, channelName2, importance2).apply {
            description = channelDescription2
        }

        notificationManager.createNotificationChannel(channel2)
    }

    // NOTIFICATIONS -> 2. Channel создан, теперь можно приступить непосредственно к созданию уведомления
    private fun createNotifications() {
        // Для задержки перед уведомлениями - postDelayed
        Handler(Looper.myLooper()!!).postDelayed({

            // 2.5. Доступаемся к NotificationManager
            val notificationManager = NotificationManagerCompat.from(requireContext())

            // 2.2. Создаём intent (activityIntent) - при клике на уведомление будет открываться SuccessActivity
            val activityIntent = Intent(requireContext(), MainActivity::class.java)
            // 2.3. Передаём activityIntent в pendingIntent. Pending intent - отложенные намерения
            val pendingIntent = PendingIntent.getActivity(requireContext(), 0, activityIntent, FLAG_CANCEL_CURRENT)

            // 2.1. Создаём переменную notification, где создадим notification c помощью билдера
            val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New message")
                .setContentText("Hello!")
                // 2.4. Передаём pendingIntent в .setContentIntent()
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            // 2.6. И передаём ему наш notification
            notificationManager.notify(0, notification)

            // NOTIFICATIONS -> Создадим второе уведомление, КАСТОМНОЕ:
            // Создадим кастомное уведомление, без действия при клике на него. Однако, при отсутствии назначеного действия уведомление не пропадёт, поэтому донастройка .setAutoCancel() не играет никакой роли, не будем её писать в билдере
            // Для начала, создадим remoteViews, с помощью которого мы будем доступаться к нашим элементам (строкам, картинкам). Можно передать requireActivity() либо requireContext()
            // Так же нужно создать отдельный layout для этого уведомления - layout_draft_notification_custom
            val remoteViews = RemoteViews(requireActivity().packageName, R.layout.layout_draft_notification_custom)
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.success_mohamed_hassan_pixabay)
            remoteViews.setTextViewText(R.id.notification_title, "Title of the notification")
            remoteViews.setTextViewText(R.id.notification_description, "Description of the notification")

            // Билдер. В .setCustomContentView() передаём наш remoteViews
            val notification2 = NotificationCompat.Builder(requireContext(), CHANNEL_ID_2)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

            notificationManager.notify(1, notification2)

        }, 5000)
    }
}