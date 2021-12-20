package com.tms.lesson01.musicgalleryapplication.mvvm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

        // Изменяем цвет ActionBar:
        // 1. Define ActionBar object
        val actionBar: ActionBar? = supportActionBar
        // 2. Define ColorDrawable object and parse color, using parseColor method
        // with color hash code as its parameter
        val colorDrawable: ColorDrawable = ColorDrawable(Color.parseColor("#1A222F"))
        // 3. Set BackgroundDrawable
        actionBar?.setBackgroundDrawable(colorDrawable)

        // Открываем первый фрагмент
        if (savedInstanceState == null) {
            openFragment(LoginFragment()) // Открываем первый фрагмент. Чистить стек не нужно, он ещё пустой
        }
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