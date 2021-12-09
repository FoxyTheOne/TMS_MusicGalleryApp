package com.tms.lesson01.musicgalleryapplication.mvvm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.fragment.LoginFragment

/**
 * hw03. Переводим наше приложение на фрагменты. Создаём общий Activity и его layout (activity_main)
 */
class MainActivity: AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val NAVIGATION_EVENT = "navigation_event" // ключ, на который подпишется MainActivity
        const val NAVIGATION_EVENT_DATA_KEY = "navigation_event_data_key" // создали ключ, по которому будем класть данны в Map (в обертку данных bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // сразу осуществляем подписку на получение данных из фрагмента по ключу NAVIGATION_EVENT
        listenNavigationEvents()

        // Открываем первый фрагмент
        openFragment(LoginFragment())
    }

    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount

        if (fragmentCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    fun openFragment(fragment: Fragment) {
        // Открываем фрагмент из Activity:
        supportFragmentManager.beginTransaction()
            // Добавляем LoginFragment в main_fragment_container, с тегом LoginFragment. Тег не обязателен. Тег можно использовать при поиске в стеке в дальнейшем
            .replace(R.id.main_fragment_container, fragment, fragment.toString())
            // Добавляем в BackStack, чтобы оставался в стеке фрагментов и при клике "назад" этот фрагмент открылся, как предыдущий
            .addToBackStack(null)
            // Исполнить транзакцию:
            .commit()
    }

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