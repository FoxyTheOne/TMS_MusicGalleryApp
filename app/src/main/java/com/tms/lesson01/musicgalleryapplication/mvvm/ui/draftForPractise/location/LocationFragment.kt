package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.tms.lesson01.musicgalleryapplication.R

/**
 * Фрагмент, где мы будем использовать Location API, а так же реализуем некоторые возможности google maps
 * LOCATION -> 1.1. Прописать необходимые разрешения в манифесте
 * LOCATION -> 1.2. Имплементировать необходимую библиотеку (play services location)
 * LOCATION -> 1.3. Для доступа к местоположению, нужно разрешение. Логика запроса разрешения - в предыдущем фрагменте
 * LOCATION -> 1.4. Получим наш FusedLocationProviderClient. Именно он имеет в себе методы, с помощью которых мы можем определить локацию
 * TRACK DISTANCE -> 2. Добавим функцию - на экран будем выводить дистанцию, которую мы прошли
 */
class LocationFragment: Fragment() {
    companion object {
        private const val TAG = "LocationFragment"
    }

    // Переменные класса
    // Переменная для нашего FusedLocationProviderClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // TRACK DISTANCE -> 2.1. Для расчёта дистанции, которую прошли (предыд. локация, пройденый путь, флажок для кнопки, а так же необходимые нам view)
    private lateinit var textPassedDistance: TextView
    private lateinit var buttonTrackDistance: AppCompatButton
    private var previousLocation: Location? = null
    private var passedDistance: Double = 0.0
    private var trackDistance = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_draft_location, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textPassedDistance = view.findViewById(R.id.text_passedDistance)
        buttonTrackDistance = view.findViewById(R.id.button_trackDistance)
        // Для динамического изменения строки необходимо оформить её должным образом: "Passed distance: %1$s"
        textPassedDistance.text = getString(R.string.location_passedDistance, passedDistance.toString())

        // LOCATION -> 1.4. Получим наш FusedLocationProviderClient. Именно он имеет в себе методы, с помощью которых мы можем определить локацию
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // LOCATION -> 1.5. Создадим метод для получения Current location либо Last location
        getCurrentOrLastLocation()
        // LOCATION -> 1.6. Создадим наш .requestLocationUpdates (настройка получения обновлений)
        updateLocation()

        // Всех наших слушателей будем выносить в отдельный метод
        setClickListeners()
    }

    private fun setClickListeners() {
        // TRACK DISTANCE -> 2.3. Повесим слушатель на кнопку, чтобы считать дистанцию при нажатии
        buttonTrackDistance.setOnClickListener {
            trackDistance = !trackDistance
            if (trackDistance) {
                buttonTrackDistance.text = getString(R.string.location_stopCalculateDistance)
            } else {
                buttonTrackDistance.text = getString(R.string.location_calculateDistance)
            }
        }
    }

    // LOCATION -> 1.5. Создадим метод для получения Current location либо Last location
    // @SuppressLint("MissingPermission") - эта аннотация означает, что мы должны точно знать, что мы уже проверили, есть ли у нас PERMISSION
    // Т.к. на этот фрагмент мы попадаем только при соглачии на получение местоположения, значит здесь разрешение у нас точно есть
    // Используем инициализированный fusedLocationProviderClient для доступа к методам
    @SuppressLint("MissingPermission")
    private fun getCurrentOrLastLocation() {
        // Last location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location ?: return@addOnSuccessListener // Т.к. location может прилететь null, пишем тернарный оператор. Если location = null, то просто выходим из метода addOnSuccessListener
            Log.d(TAG, "LastLocation: latitude = ${location.latitude}, longitude = ${location.longitude}") // Проверяем получение ширины и долготы в логе
        }

        // Current location
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                // Создадим переменную со значением по умолчанию
                private var isCancellationRequested = false

                // Если запрошено
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    isCancellationRequested = true
                    return this
                }

                // Проверка статуса
                override fun isCancellationRequested(): Boolean {
                    return isCancellationRequested
                }
            }).addOnSuccessListener { location: Location? ->
            // Т.к. location может прилететь null, пишем тернарный оператор. Если location = null, то просто выходим из метода addOnSuccessListener
            location ?: return@addOnSuccessListener
            // Проверяем получение ширины и долготы в логе
            Log.d( TAG, "LastLocation: latitude = ${location.latitude}, longitude = ${location.longitude}")
        }
    }

    // LOCATION -> 1.6. Создадим наш .requestLocationUpdates (настройка получения обновлений)
    // Для этого нам сначала нужно сформировать первые два параметра - locationRequest и locationCallback
    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        // init locationRequest (1 параметр)
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // init locationCallback (2 параметр)
        val locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // Найдем последнюю локацию
                val updatedLocation = locationResult.lastLocation
                // Выведем её в лог
                Log.d(TAG, "Updated location: latitude = ${updatedLocation.latitude}, longitude = ${updatedLocation.longitude} ")

                // TRACK DISTANCE -> 2.2. Если наша previousLocation не null, будем сравнивать дистанцию и запишем логику расчёта
                previousLocation?.let { previousLocation ->
                    // Считаем, только если нажали кнопку и установили флажок "true"
                    if (trackDistance) {
                        passedDistance =
                            passedDistance + updatedLocation.distanceTo(previousLocation)
                        // И меняем текст на экране после подсчёта. Для того, чтобы это сработало, строку необходимо оформить должным образом: "Passed distance: %1$s"
                        textPassedDistance.text =
                            getString(R.string.location_passedDistance, passedDistance.toString())
                    }
                }
                // И заменяем старое значение новым
                previousLocation = updatedLocation
            }
        }

        // Непосредственно настройка получения обновлений (ключевой метод):
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}