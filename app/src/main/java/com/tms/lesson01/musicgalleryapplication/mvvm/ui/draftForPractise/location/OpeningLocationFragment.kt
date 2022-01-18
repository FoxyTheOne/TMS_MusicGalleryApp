package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity

/**
 * 1. Декларируем в Manifest PERMISSION для определения местоположения
 * 2. Пишем логику запроса разрешения на чтение местоположения
 *
 * !!! Если необходим запрос на BACKGROUND location, его всегда нужно делать отдельно и только после
 * либо ACCESS_COARSE_LOCATION, либо ACCESS_FINE_LOCATION.
 * Только после позитивного ответа на один из них, можно делать запрос на BACKGROUND location permission !!!
 */
class OpeningLocationFragment: Fragment() {
    // Переменные класса
    private lateinit var buttonSeeMap: AppCompatButton
    // Переменная для запроса BACKGROUND location permission
    private lateinit var requestBackgroundLocationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_draft_location_open, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация переменных
        buttonSeeMap = view.findViewById(R.id.button_seeMap)

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()

        // Оформим запрос на PERMISSION, если он не был дан в предыдущий раз
        // !!! Т.к. запросов много, а не один, мы пишем .RequestMultiplePermissions() вместо .RequestPermission()
        // Т.обр., в лямбду к нам залетает не boolean, а map. ключом этого map будет string (наши permissions), а второе значение - это boolean
        // Следовательно, для обращения к определенному PERMISSION, мы обращаемся к нему по ключу типа permissionsMap[...] == true
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionsMap ->
                if (permissionsMap[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    ||
                    permissionsMap[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    // Если дано одно из разрешений, открываем следующий фрагмент
//                    requestBackgroundLocationPermission()
                    openLocationFragment()
                } else {
                    Toast.makeText(requireContext(), "We have no access to your location", Toast.LENGTH_LONG).show()
                }
            }

        // Кнопка перехода на LocationFragment
        buttonSeeMap.setOnClickListener {
            // Если одно из разрешений уже есть, открываем LocationFragment
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
//                requestBackgroundLocationPermission()
                openLocationFragment()
            } else {
                // Если нет - вызываем requestPermissionLauncher
                requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ))
            }
        }
    }

    // private функции - только для OpeningLocationFragment:
    private fun openLocationFragment() {
        (activity as MainActivity).openFragment(LocationFragment())
    }

    // отправляем данные в MainActivity, которое подписано на ключ NAVIGATION_EVENT
    private fun sendNavigationEvents() {
        requireActivity().supportFragmentManager.setFragmentResult( // Пишем requireActivity() вместо activity, т.к. activity м.б. null и нужна будет дополнительная проверка
            // Буду отправлять на слушателя с ключом NAVIGATION_EVENT (т.е. это ключ, по которому мы регистрировали слушателя ранее)
            MainActivity.NAVIGATION_EVENT,
            // В bundle указываемключ-значение данных, которые хотим передать
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "OpeningLocationFragment created")
        )
    }

    // Запрос на BACKGROUND location permission. Для примера, нам он сейчас не нужен
    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundLocationPermissionLauncher.launch(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
}