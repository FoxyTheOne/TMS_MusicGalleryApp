package com.tms.lesson01.musicgalleryapplication.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.fragment.LoginFragment

/**
 * hw03. Переводим наше приложение на фрагменты. Создаём общий Activity и его layout (activity_main)
 */
class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openFragment(LoginFragment())
    }

    fun openFragment(fragment: Fragment) {
        // Открываем фрагмент из Activity:
        supportFragmentManager.beginTransaction()
            // Добавляем LoginFragment в main_fragment_container, с тегом LoginFragment. Тег не обязателен. Тег можно использовать при поиске в стеке в дальнейшем
            .add(R.id.main_fragment_container, fragment, fragment.toString())
            // Добавляем в BackStack, чтобы оставался в стеке фрагментов и при клике "назад" этот фрагмент открылся, как предыдущий
            .addToBackStack(null)
            // Исполнить транзакцию:
            .commit()
    }

    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount

        if (fragmentCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }
}