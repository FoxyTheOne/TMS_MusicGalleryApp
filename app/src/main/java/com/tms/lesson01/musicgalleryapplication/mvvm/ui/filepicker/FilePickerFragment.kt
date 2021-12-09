package com.tms.lesson01.musicgalleryapplication.mvvm.ui.filepicker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import droidninja.filepicker.FilePickerBuilder

class FilePickerFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_file_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()

        // Запрос на разрешение доступа к Storage, если он не был дан в предыдущий раз
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    openFilePicker()
                } else {
                    Toast.makeText(requireContext(), "We have no access to storage", Toast.LENGTH_LONG).show()
                }
            }


        // Оглашаем наши локальные переменные
        val viewFilesButton: AppCompatButton = view.findViewById(R.id.button_filePickerViewFiles)

        // Кнопка перехода на Галерею
        viewFilesButton.setOnClickListener {
            // Если разрешение уже есть, открываем Галерею - openFilePicker()
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openFilePicker()
            } else {
                // Если нет - вызываем requestPermissionLauncher
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    // private функции - только для BrowserFragment:
    // отправляем данные в MainActivity, которое подписано на ключ NAVIGATION_EVENT
    private fun sendNavigationEvents() {
        requireActivity().supportFragmentManager.setFragmentResult( // Пишем requireActivity() вместо activity, т.к. activity м.б. null и нужна будет дополнительная проверка
            // Буду отправлять на слушателя с ключом NAVIGATION_EVENT (т.е. это ключ, по которому мы регистрировали слушателя ранее)
            MainActivity.NAVIGATION_EVENT,
            // В bundle указываемключ-значение данных, которые хотим передать
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "FilePickerFragment created")
        )
    }

    private fun openFilePicker() {
            // Сюда копируем соответствующий код с сайта
        FilePickerBuilder.instance
            .setMaxCount(5)
            .setActivityTheme(R.style.LibAppTheme)
            .pickPhoto(this);
    }
}