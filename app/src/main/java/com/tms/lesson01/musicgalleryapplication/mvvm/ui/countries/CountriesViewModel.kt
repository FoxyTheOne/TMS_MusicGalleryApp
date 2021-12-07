package com.tms.lesson01.musicgalleryapplication.mvvm.ui.countries

import android.util.Log
import androidx.lifecycle.*

class CountriesViewModel: ViewModel(), LifecycleEventObserver {
    // Создаём LiveData, которое слушает Activity:
    val countriesLiveData = MutableLiveData<List<String>>()
    // Наш экземпляр класса Model для обращения к ней. Ти переменной - наш интерфейс:
    val model: ICountriesModel = CountriesModel()

    // В этом классе мы хотим слушать события, поэтому наследуемся от LifecycleEventObserver и override метод onStateChanged
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        // Жизненный цикл нашего activity:
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                println("ON_CREATE")
                getCountries() // метод для вывода на экран наш список при создании activity
            }
            Lifecycle.Event.ON_START -> {
                println("ON_START")
            }
            Lifecycle.Event.ON_RESUME -> {
                println("ON_RESUME")
            }
            Lifecycle.Event.ON_PAUSE -> {
                println("ON_PAUSE")
            }
            Lifecycle.Event.ON_STOP -> {
                println("ON_STOP")
            }
            Lifecycle.Event.ON_DESTROY -> {
                println("ON_DESTROY")
            }
            Lifecycle.Event.ON_ANY -> {
                println("ON_ANY")
            }
        }
    }

    private fun getCountries() {
        val countries = model.getCountries() // Получаем список
        countriesLiveData.value = countries // Передаём список в LiveData, чтобы передать в activity
    }
}