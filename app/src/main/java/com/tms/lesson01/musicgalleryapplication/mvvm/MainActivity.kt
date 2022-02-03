package com.tms.lesson01.musicgalleryapplication.mvvm

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.AppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.fragment.LoginFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment.PlaylistsListFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.WeatherWidget
import android.view.WindowManager

import android.os.Build
import android.view.Window
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.IAppSharedPreferences
import org.koin.android.ext.android.inject

/**
 * hw03. Переводим наше приложение на фрагменты. Создаём общий Activity и его layout (activity_main)
 */
class MainActivity: AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val NAVIGATION_EVENT = "navigation_event" // ключ, на который подпишется MainActivity
        const val NAVIGATION_EVENT_DATA_KEY = "navigation_event_data_key" // создали ключ, по которому будем класть данны в Map (в обертку данных bundle)
    }
    // Инициализируем sharedPreferences с помощью Koin
    private val sharedPreferences by inject<IAppSharedPreferences>()

    // 1.1 Define ActionBar object
    var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // сразу осуществляем подписку на получение данных из фрагмента по ключу NAVIGATION_EVENT
        listenNavigationEvents()

        // Прозрачный Status Bar -> In Activity's onCreate() for instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        // Изменяем цвет ActionBar:
        // 1.2 Define ActionBar object
        actionBar = supportActionBar
        // 2. Define ColorDrawable object and parse color, using parseColor method
        // with color hash code as its parameter
        val colorDrawable: ColorDrawable = ColorDrawable(Color.parseColor("#1A222F"))
        // 3. Set BackgroundDrawable
        actionBar?.setBackgroundDrawable(colorDrawable)

        // Открываем первый фрагмент
        if (savedInstanceState == null) {
            if (sharedPreferences.getToken().isBlank()) {
                openFragment(LoginFragment()) // Открываем первый фрагмент. Чистить стек не нужно, он ещё пустой
                // 4. Скрываем ActionBar для первого фрагмента:
                actionBar?.hide()
            } else {
                openFragment(PlaylistsListFragment())
            }
        }

        // WIDGET -> Обновление виджета программно
        updateProgrammaticallyHomeWidget()
    }



    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount

        if (fragmentCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    fun openFragment(fragment: Fragment, doClearBackStack: Boolean = false) { // Задаём параметр по умолчанию - если не передать true для очистки стека, по умолчанию будет false
        if (doClearBackStack){ // Если передали true, чистим стек
            clearBackStack()
        }

        // Открываем фрагмент из Activity:
        supportFragmentManager.beginTransaction()
            .setCustomAnimations( // Анимации по умолчанию для всех фрагментов
                R.anim.slide_in, // Как будет входить новый элемент
                R.anim.fade_out, // Как будет пропадать предыдущий
                R.anim.fade_in_while_back_clicked, // Как будет появл предыд фрагмент при клике на back
                R.anim.slide_out_while_back_clicked // Как во время back будет пропадать тот фрагмент, к-рый был наверху
            )
            // Добавляем LoginFragment в main_fragment_container, с тегом LoginFragment. Тег не обязателен. Тег можно использовать при поиске в стеке в дальнейшем
            .replace(R.id.main_fragment_container, fragment, fragment.toString())
            // Добавляем в BackStack, чтобы оставался в стеке фрагментов и при клике "назад" этот фрагмент открылся, как предыдущий
            .addToBackStack(null)
            // Исполнить транзакцию:
            .commit()
        // 5. Показываем ActionBar для остальных фрагментов:
        actionBar?.show()
    }

    private fun updateProgrammaticallyHomeWidget() {
        // Чтобы обновить наш виджет, нам нужно кинуть бродкаст с интент фильтром APPWIDGET_UPDATE
        // Сделаем задержку (postDelayed)
        Handler(Looper.myLooper()!!).postDelayed({
            // Говорим, что будем обращаться к WeatherWidget
            val intentWidgetUpdate = Intent(this, WeatherWidget::class.java).apply {
                // Передаём id
                val ids: IntArray = AppWidgetManager.getInstance(application)
                    .getAppWidgetIds(ComponentName(application, WeatherWidget::class.java))
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids)
            }
            sendBroadcast(intentWidgetUpdate)
        }, 5000)
    }

    private fun clearBackStack() = supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    // private функции - только для MainActivity:
    // Подписка на получение данных из фрагмента (слушаем всё по ключу NAVIGATION_EVENT, т.е подписываемся на него)
    private fun listenNavigationEvents() {
        // requestKey использовать не будем, поэтому "_". bundle - обертка, внутри которой будут данные, которые прилетают. Достаём из нее данные по ключу NAVIGATION_EVENT_DATA_KEY
        supportFragmentManager.setFragmentResultListener(NAVIGATION_EVENT, this, { _, bundle ->
            val navigationEvent = bundle.get(NAVIGATION_EVENT_DATA_KEY)
            Log.d(TAG, navigationEvent.toString()) // Выводим результат
        })
    }
}