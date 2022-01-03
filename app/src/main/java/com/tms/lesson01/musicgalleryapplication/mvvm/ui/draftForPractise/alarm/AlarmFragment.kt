package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tms.lesson01.musicgalleryapplication.R
import java.util.*

class AlarmFragment: Fragment() {
    companion object {
        private const val TAG = "AlarmFragment"
        private const val FILTER_VALUE = "AlarmReceiver"
        private const val REQUEST_CODE = 101
    }

    // ViewModel становится общей для всех, кто создавал её подобным образом
    private val viewModel by activityViewModels<AlarmViewModel>()
    private lateinit var buttonOpenTimePicker: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_draft_alarm, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Регистрируем наш Broadcast. Он будет работать только с теми интентами, у которых такой фильтр
        requireContext().registerReceiver(AlarmBroadcast(), IntentFilter(FILTER_VALUE))

        buttonOpenTimePicker = view.findViewById(R.id.button_openTimePicker)

        initListeners()
        subscribeToLiveData()
    }

    private fun initListeners() {
        buttonOpenTimePicker.setOnClickListener {
            val timePicker = TimePickerDialogFragment()
            timePicker.show(requireActivity().supportFragmentManager, TAG)
        }
    }

    private fun subscribeToLiveData() {
        viewModel.selectedTimeLiveData.observe(this, { selectedTime ->
            selectedTime?.let {
                // selectedTime - время в часах и минутах. Но нам нужно передать полноценное время, в милисекундах. Поэтому будем использовать Calendar с донастройками
                val calendar = Calendar.getInstance().apply {
                    set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE), it.hours, it.minutes)
                }
                val hours = calendar.get(Calendar.HOUR_OF_DAY)
                val minutes = calendar.get(Calendar.MINUTE)
                Log.d(TAG, "Selected time: $hours, $minutes")

                // Передаём время в наш метод, в милисекундах
                scheduleAlarm(calendar.timeInMillis)
            }
        })
    }

    private fun scheduleAlarm(time: Long) {
        // Инициализация AlarmManager
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        // Создаём intent
        // Присваиваем наше значение фильтру, чтобы наш Broadcast с ним работал
        val intent = Intent(requireContext(), AlarmBroadcast::class.java).apply {
            action = FILTER_VALUE
        }

        // REQUEST_CODE - уникальный код нашего запроса
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), REQUEST_CODE, intent, 0)
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        // Теперь, когда у нас есть pendingIntent, мы можем обратиться к инициализированному ранее alarmManager и зарегистрировать наш будильник(событие)
    }
}

class AlarmBroadcast: BroadcastReceiver() {
    // Полученное событие залетит сюда
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Alarm received", Toast.LENGTH_LONG).show()
    }
}