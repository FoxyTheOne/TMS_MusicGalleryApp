package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.success.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity

/**
 * Создаём новый экран SuccessLoginActivity
 * hw03. Переводим наше приложение на фрагменты.
 */
class SuccessFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_draft_success_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()
    }

    // private функции - только для SuccessFragment:
    // отправляем данные в MainActivity, которое подписано на ключ NAVIGATION_EVENT
    private fun sendNavigationEvents() {
        requireActivity().supportFragmentManager.setFragmentResult( // Пишем requireActivity() вместо activity, т.к. activity м.б. null и нужна будет дополнительная проверка
            // Буду отправлять на слушателя с ключом NAVIGATION_EVENT (т.е. это ключ, по которому мы регистрировали слушателя ранее)
            MainActivity.NAVIGATION_EVENT,
            // В bundle указываемключ-значение данных, которые хотим передать
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "SuccessFragment  created")
        )
    }
}