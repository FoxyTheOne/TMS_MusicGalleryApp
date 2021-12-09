package com.tms.lesson01.musicgalleryapplication.mvvm.ui.countries.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.browser.BrowserFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.countries.CountriesViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.success.fragment.SuccessFragment

class CountriesFragment : Fragment() {
    // Переменные класса
    private lateinit var viewModel: CountriesViewModel
    private lateinit var countriesListView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_countries, container, false)
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
        viewModel = ViewModelProvider(this)[CountriesViewModel::class.java]
        // Этот активити будет слушать наша view model, поэтому регистрируем слушателя здесь:
        lifecycle.addObserver(viewModel)

        // Оглашаем наши локальные переменные
        val openSuccessButton: AppCompatButton = view.findViewById(R.id.button_openSuccess)
        val openURLButton: AppCompatButton = view.findViewById(R.id.button_openBrowserFragment)
        countriesListView = view.findViewById(R.id.list_countries)

        // Кнопка перехода на success
        openSuccessButton.setOnClickListener {
            (activity as MainActivity).openFragment(SuccessFragment())
        }
        // Кнопка перехода на BrowserFragment
        openURLButton.setOnClickListener {
            (activity as MainActivity).openFragment(BrowserFragment())
        }

        subscribeOnLiveData()
    }

    // private функции - только для CountriesFragment:
    // Подписка на LiveData
    private fun subscribeOnLiveData() {
        viewModel.countriesLiveData.observe(viewLifecycleOwner, { countries ->
            val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, countries)
            countriesListView.adapter = adapter
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