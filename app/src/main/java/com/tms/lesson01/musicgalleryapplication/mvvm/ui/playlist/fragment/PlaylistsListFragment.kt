package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.AppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.AppDatabase
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.INetworkMusicService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.music.NetworkMusicServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.lastFM.ILastFMNetwork
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.playlistLastFM.lastFM.LastFMNetwork
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.browser.BrowserFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.alarm.AlarmFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.otherApplicationComponent.broadcastReceiver.BroadcastFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.PlaylistsListViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.filepicker.FilePickerFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.location.OpeningLocationFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.otherApplicationComponent.contenctProvider.OpeningContactsFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.otherApplicationComponent.serviceAndNotification.NotificationFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.fragment.LoginFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.success.fragment.SuccessFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.weather.OpeningWeatherFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.PlaylistsListViewModelFactory

/**
 * 1.3. Настройка View Model в соответствии с принципом обеспечения зависимостей:
 * Создаём объект View Model с помощью своей фабрики
 */
class PlaylistsListFragment : Fragment() {
    // Константы
    companion object {
        private const val TAG = "PlaylistsListFragment"
    }

    // Переменные класса
    private lateinit var openSuccessButton: AppCompatButton
    private lateinit var openURLButton: AppCompatButton
    private lateinit var openFilePickerButton: AppCompatButton
    private lateinit var openTimePickerButton: AppCompatButton
    private lateinit var openNotificationFragment: AppCompatButton
    private lateinit var openBroadcastFragment: AppCompatButton
    private lateinit var openContactsFragment: AppCompatButton
    private lateinit var openLocationFragment: AppCompatButton
    private lateinit var openWeatherFragment: AppCompatButton
    private lateinit var buttonLogOut: AppCompatButton
    //    private lateinit var viewModel: PlaylistsListViewModel
    private lateinit var yourFavoritesRecyclerView: RecyclerView // 1. Создадим RecyclerView для наших горизонтальных списков
    private lateinit var recommendedPlaylistsRecyclerView: RecyclerView
    private lateinit var artistRecyclerView: RecyclerView
    private var adapterYourFavouritesPlaylist: YourFavouritesPlaylistRecyclerAdapter? = null // 4. Определим для нашего RecyclerView созданный адаптер
    private var adapterRecommendedPlaylist: RecommendedPlaylistRecyclerAdapter? = null // 4. Определим для нашего RecyclerView созданный адаптер
    private var adapterArtists: TopArtistRecyclerAdapter? = null // 4. Определим для нашего RecyclerView созданный адаптер
    private val lastFMNetwork = LastFMNetwork.getInstance() as ILastFMNetwork

    // Инициализируем View Model с помощью своей фабрики
    private val viewModel by viewModels<PlaylistsListViewModel> {
        PlaylistsListViewModelFactory(
            // Инициализируем объекты в конструкторе
            NetworkMusicServiceModel() as INetworkMusicService, // Наш экземпляр класса Model для обращения к ней. Тип переменной - наш интерфейс. NetworkMusicServiceModel() будет возвращать нам альбомы
            // Объект preferences для обращения к SharedPreferences
            AppSharedPreferences.getInstance(requireContext()),  // Вызываем наш статический метод для экземпляра класса AppSharedPreferences

            // Объекты для обращения к Dao
            AppDatabase.getInstance(requireContext()).getYourFavouritesPlaylistDao(), // Вызываем наш статический метод для экземпляра класса AppDatabase и затем обращаемся к Dao
            AppDatabase.getInstance(requireContext()).getRecommendedPlaylistDao(), // Вызываем наш статический метод для экземпляра класса AppDatabase и затем обращаемся к Dao

            // Объекты для обращения к сервисам
            // lastFM
            lastFMNetwork.getArtistService()
        )
    }

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

//        !!! Этот код (инициализация сущностей, создание объектов) переносим в конструктор View Model:
//        // Инициализируем viewModel, чтобы работать с ViewModel (согласно примеру с сайта)
//        viewModel = ViewModelProvider(this)[PlaylistsListViewModel::class.java]

        // Этот активити будет слушать наша view model, поэтому регистрируем слушателя здесь:
        lifecycle.addObserver(viewModel)

//        viewModel.setSharedPreferences(AppSharedPreferences.getInstance(requireContext())) // Вызываем наш статический метод для экземпляра класса AppSharedPreferences
//        viewModel.setYourFavouritesPlaylistDao(AppDatabase.getInstance(requireContext()).getYourFavouritesPlaylistDao()) // Вызываем наш статический метод для экземпляра класса AppDatabase и затем обращаемся к Dao
//        viewModel.setRecommendedPlaylistDao(AppDatabase.getInstance(requireContext()).getRecommendedPlaylistDao()) // Вызываем наш статический метод для экземпляра класса AppDatabase и затем обращаемся к Dao
//        // lastFM
//        val lastFMNetwork = LastFMNetwork.getInstance() as ILastFMNetwork
//        viewModel.setArtistService(lastFMNetwork.getArtistService())

        // Оглашаем наши локальные переменные
        openSuccessButton = view.findViewById(R.id.button_openSuccess)
        openURLButton = view.findViewById(R.id.button_openBrowserFragment)
        openFilePickerButton = view.findViewById(R.id.button_openFilePickerFragment)
        openTimePickerButton = view.findViewById(R.id.button_openTimePickerFragment)
        openNotificationFragment = view.findViewById(R.id.button_openNotificationFragment)
        openBroadcastFragment = view.findViewById(R.id.button_openBroadcastFragment)
        openContactsFragment = view.findViewById(R.id.button_openContactsFragment)
        openLocationFragment = view.findViewById(R.id.button_openLocationFragment)
        openWeatherFragment = view.findViewById(R.id.button_openWeatherFragment)
        buttonLogOut = view.findViewById(R.id.button_logOut)
        yourFavoritesRecyclerView = view.findViewById(R.id.list_yourFavorites) // 2. Первый RecyclerView. Далее создаём адаптер (YourFavouritesPlaylistRecyclerAdapter)
        recommendedPlaylistsRecyclerView = view.findViewById(R.id.list_recommendedPlaylists) // 2. Второй RecyclerView. Далее создаём адаптер (RecommendedPlaylistRecyclerAdapter)
        artistRecyclerView = view.findViewById(R.id.list_topArtists) // 2. Третий RecyclerView. Далее создаём адаптер (TopArtistRecyclerAdapter)

        // Делаем RecyclerView1 горизонтальным
        yourFavoritesRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        // Делаем RecyclerView2 горизонтальным
        recommendedPlaylistsRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        // Делаем RecyclerView3 горизонтальным
        artistRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        artistRecyclerView.addItemDecoration(TopArtistRecyclerItemDecoration(16))

        initListeners()
        subscribeOnLiveData()
    }

    private fun initListeners() {
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
        // Кнопка перехода на TimePickerFragment
        openTimePickerButton.setOnClickListener {
            (activity as MainActivity).openFragment(AlarmFragment())
        }
        // Кнопка перехода на NotificationFragment
        openNotificationFragment.setOnClickListener {
            (activity as MainActivity).openFragment(NotificationFragment())
        }
        // Кнопка перехода на BroadcastFragment
        openBroadcastFragment.setOnClickListener {
            (activity as MainActivity).openFragment(BroadcastFragment())
        }
        // Кнопка перехода на ContactsFragment
        openContactsFragment.setOnClickListener {
            (activity as MainActivity).openFragment(OpeningContactsFragment())
        }
        // Кнопка перехода на LocationFragment
        openLocationFragment.setOnClickListener {
            (activity as MainActivity).openFragment(OpeningLocationFragment())
        }
        // Кнопка перехода на WeatherFragment
        openWeatherFragment.setOnClickListener {
            (activity as MainActivity).openFragment(OpeningWeatherFragment())
        }
        buttonLogOut.setOnClickListener {
            viewModel.logOut()
        }
    }

    // private функции - только для CountriesFragment:
    // Подписка на LiveData
    private fun subscribeOnLiveData() {
        viewModel.yourFavoritesLiveData.observe(viewLifecycleOwner, { playlists ->
            adapterYourFavouritesPlaylist = YourFavouritesPlaylistRecyclerAdapter(playlists) { playlist -> Log.d(TAG, playlist.toString())} // в конструкторе PlaylistsRecyclerAdapter описываем нашу анонимную функцию
            yourFavoritesRecyclerView.adapter = adapterYourFavouritesPlaylist
        })
        viewModel.recommendedPlaylistsLiveData.observe(viewLifecycleOwner, { playlists ->
            adapterRecommendedPlaylist = RecommendedPlaylistRecyclerAdapter(playlists) { playlist -> Log.d(TAG, playlist.toString())}
            recommendedPlaylistsRecyclerView.adapter = adapterRecommendedPlaylist
        })
        viewModel.artistsLiveData.observe(viewLifecycleOwner, { artists ->
            adapterArtists = TopArtistRecyclerAdapter(artists) { artist -> Log.d(TAG, artist.toString()) }
            artistRecyclerView.adapter = adapterArtists
        })
        // Если услышим logOutLiveData, значит надо выйти из приложения (открыть логин экран и почистить стек)
        viewModel.logOutLiveData.observe(viewLifecycleOwner,{
            (activity as MainActivity).openFragment(LoginFragment(), true)
            (activity as MainActivity).actionBar?.hide()
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