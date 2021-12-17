package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.activityOld

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.PlaylistsListViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.success.activity.SuccessLoginActivity

class CountriesActivity: AppCompatActivity() {
    // Переменные класса
    private lateinit var viewModel: PlaylistsListViewModel
    private lateinit var countriesListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_playlists)
        // Инициализируем viewModel, чтобы работать с ViewModel (согласно примеру с сайта)
        viewModel = ViewModelProvider(this)[PlaylistsListViewModel::class.java]
        // Этот активити будет слушать наша view model, поэтому регистрируем слушателя здесь:
        lifecycle.addObserver(viewModel)

        // Оглашаем наши локальные переменные
        val openSuccessButton: AppCompatButton = findViewById(R.id.button_openSuccess)
        countriesListView = findViewById(R.id.list_yourFavorites)

        // Кнопка перехода на success
        openSuccessButton.setOnClickListener {
            val intent = Intent(this, SuccessLoginActivity::class.java)
            startActivity(intent)
        }

//        subscribeOnLiveData()
    }

//    private fun subscribeOnLiveData() {
//        viewModel.countriesLiveData.observe(this, { countries ->
//            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, countries)
//            countriesListView.adapter = adapter
//        })
//    }
}