package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.modelUI.SelectedTime

class AlarmViewModel: ViewModel() {
    // Инициализация будет не тогда, когда создается AlarmViewModel. А тогда, когда обратятся к этой переменной:
    val selectedTimeLiveData by lazy { MutableLiveData<SelectedTime>() }

    fun onTimeSelected(selectedTime: SelectedTime){
        // Используем value, а не postValue, т.к. всегда будет выполняться в основном потоке
        selectedTimeLiveData.value = selectedTime
    }
}