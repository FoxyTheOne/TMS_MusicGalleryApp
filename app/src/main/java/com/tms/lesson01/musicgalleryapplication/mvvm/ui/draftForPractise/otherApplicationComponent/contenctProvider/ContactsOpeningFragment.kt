package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.otherApplicationComponent.contenctProvider

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

/**
 * 1. Декларируем в Manifest PERMISSION для чтения контактов
 */
class ContactsOpeningFragment: Fragment() {

    // Переменные класса
    private lateinit var buttonSeeContacts: AppCompatButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_draft_contacts_open, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация переменных
        buttonSeeContacts = view.findViewById(R.id.button_seeContacts)

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()

        // Оформим запрос на PERMISSION, если он не был дан в предыдущий раз
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    openContactsFragment()
                } else {
                    Toast.makeText(requireContext(), "We have no access to contacts", Toast.LENGTH_LONG).show()
                }
            }

        // Кнопка перехода на ContactsFragment
        buttonSeeContacts.setOnClickListener {
            // Если разрешение уже есть, открываем ContactsFragment
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openContactsFragment()
            } else {
                // Если нет - вызываем requestPermissionLauncher
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    // private функции - только для ContactsOpeningFragment:
    // 2. Для доступа к контактам, нам нужно разрешение. Первым делом нужно его запросить. Создадим метод readContacts() и вызовем его в onViewCreated()
    private fun openContactsFragment() {
        (activity as MainActivity).openFragment(ContactsFragment())
    }

    // отправляем данные в MainActivity, которое подписано на ключ NAVIGATION_EVENT
    private fun sendNavigationEvents() {
        requireActivity().supportFragmentManager.setFragmentResult( // Пишем requireActivity() вместо activity, т.к. activity м.б. null и нужна будет дополнительная проверка
            // Буду отправлять на слушателя с ключом NAVIGATION_EVENT (т.е. это ключ, по которому мы регистрировали слушателя ранее)
            MainActivity.NAVIGATION_EVENT,
            // В bundle указываемключ-значение данных, которые хотим передать
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "ContactsOpeningFragment created")
        )
    }
}