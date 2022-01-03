package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.alarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.modelUI.SelectedTime
import java.util.*

class TimePickerDialogFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {
    // ViewModel становится общей для всех, кто создавал её подобным образом
    private val viewModel by activityViewModels<AlarmViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    // В методе onTimeSet нужно передать событие во ViewModel. Для этого нужно создать метод во ViewModel, а так же создать модель, которая будет использоваться в слое UI
    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Выбрав какое-то время, оно прилетит в этот метод
        viewModel.onTimeSelected(SelectedTime(hourOfDay, minute))
    }
}