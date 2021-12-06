package com.tms.lesson01.musicgalleryapplication.mvvm.ui.success.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R

/**
 * Создаём новый экран SuccessLoginActivity
 * hw03. Переводим наше приложение на фрагменты.
 */
class SuccessFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_success_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}