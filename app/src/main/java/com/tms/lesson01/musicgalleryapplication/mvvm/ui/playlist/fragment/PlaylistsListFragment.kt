package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.browser.BrowserFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.PlaylistsListViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.filepicker.FilePickerFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.success.fragment.SuccessFragment

class PlaylistsListFragment : Fragment() {
    // Константы
    companion object {
        private const val TAG = "PlaylistsListFragment"
    }

    // Переменные класса
    private lateinit var viewModel: PlaylistsListViewModel
    private lateinit var yourFavoritesRecyclerView: RecyclerView // 1. Создадим RecyclerView для наших горизонтальных списков
    private lateinit var recommendedPlaylistsRecyclerView: RecyclerView
    private var adapter: PlaylistsRecyclerAdapter? = null // 4. Определим для нашего RecyclerView созданный адаптер

    // определяем вид экрана (layout)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Сюда копируем код из onCreate в Activity. Перед findViewById нужно добавить "view."
        // В методе subscribeOnLiveData() (если такой есть):
        // Вместо "this" в методе .observe(), как было в Activity, пишем "viewLifecycleOwner"
        // Там, где мы в Activity передавали context, писали "this". Во фрагменте нужно писать "context"
        // Однако, context может быть null. Если нужно значение точно не null(подчеркнет красным), пишем "requireContext()"

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()

        // Инициализируем viewModel, чтобы работать с ViewModel (согласно примеру с сайта)
        viewModel = ViewModelProvider(this)[PlaylistsListViewModel::class.java]
        // Этот активити будет слушать наша view model, поэтому регистрируем слушателя здесь:
        lifecycle.addObserver(viewModel)

        // Оглашаем наши локальные переменные
        val openSuccessButton: AppCompatButton = view.findViewById(R.id.button_openSuccess)
        val openURLButton: AppCompatButton = view.findViewById(R.id.button_openBrowserFragment)
        val openFilePickerButton: AppCompatButton = view.findViewById(R.id.button_openFilePickerFragment)
        yourFavoritesRecyclerView = view.findViewById(R.id.list_yourFavorites) // 2. Первый RecyclerView
        recommendedPlaylistsRecyclerView = view.findViewById(R.id.list_recommendedPlaylists) // 2. Второй RecyclerView. Далее создаём адаптер (PlaylistsRecyclerAdapter)

        // Делаем RecyclerView1 горизонтальным
        yourFavoritesRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        // Делаем RecyclerView2 горизонтальным
        recommendedPlaylistsRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        // Кнопка перехода на success
        openSuccessButton.setOnClickListener {
            (activity as MainActivity).openFragment(SuccessFragment())
        }
        // Кнопка перехода на BrowserFragment
        openURLButton.setOnClickListener {
            (activity as MainActivity).openFragment(BrowserFragment())
        }
        // Кнопка перехода на FilePickerFragment
        openFilePickerButton.setOnClickListener {
            (activity as MainActivity).openFragment(FilePickerFragment())
        }

        subscribeOnLiveData()
    }

    // private функции - только для CountriesFragment:
    // Подписка на LiveData
    private fun subscribeOnLiveData() {
        viewModel.yourFavoritesLiveData.observe(viewLifecycleOwner, { playlists ->
            adapter = PlaylistsRecyclerAdapter(playlists) {playlist -> Log.d(TAG, playlist.toString())} // в конструкторе PlaylistsRecyclerAdapter описываем нашу анонимную функцию
            yourFavoritesRecyclerView.adapter = adapter
        })
        viewModel.recommendedPlaylistsLiveData.observe(viewLifecycleOwner, { playlists ->
            adapter = PlaylistsRecyclerAdapter(playlists) {playlist -> Log.d(TAG, playlist.toString())}
            recommendedPlaylistsRecyclerView.adapter = adapter
        })
    }

    // отправляем данные в MainActivity, которое подписано на ключ NAVIGATION_EVENT
    private fun sendNavigationEvents() {
        requireActivity().supportFragmentManager.setFragmentResult( // Пишем requireActivity() вместо activity, т.к. activity м.б. null и нужна будет дополнительная проверка
            // Буду отправлять на слушателя с ключом NAVIGATION_EVENT (т.е. это ключ, по которому мы регистрировали слушателя ранее)
            MainActivity.NAVIGATION_EVENT,
            // В bundle указываемключ-значение данных, которые хотим передать
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "CountriesFragment created")
        )
    }
}