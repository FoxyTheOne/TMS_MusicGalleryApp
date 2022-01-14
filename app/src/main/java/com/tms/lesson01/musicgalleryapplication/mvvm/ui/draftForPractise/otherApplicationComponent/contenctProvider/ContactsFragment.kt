package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.otherApplicationComponent.contenctProvider

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity

/**
 * Фрагмент для чтения контактов через Content Provider
 */
class ContactsFragment: Fragment() {
    companion object {
        private const val TAG = "ContactsFragment"
    }

    // Переменные класса
    private val contactDataList = mutableListOf<ContactData>() // Список для сохранения объектов - контакт с номерами телефонов
    private lateinit var contactsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_draft_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация переменных
        contactsRecyclerView = view.findViewById(R.id.contacts_recyclerView) // Делаем стандартный вертикальный RecyclerView, поэтому layoutManager объявили в xml

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()

        // Если мы зашли на эту страницу, значит разрешение есть
        readContacts()
    }

    private fun readContacts() {
        // Теоретически мы можем столкнуться с ошибками, поэтому обернём процесс в try-catch
        try {
            // 1. !!! Достанем контакты. Для этого находим CONTENT RESOLVER
            // Т.к. мы не в активити, а во фрагменте, сначала необходимо обратиться к активити, а затем уже к contentResolver
            val myContactsCursorLoader = requireActivity().contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, // Передаём uri
                null,
                null,
                null,
                null
            )

            // 2. Мы получили таблицу строк. Теперь мы можем к ней обращаться и пробегаться по полям (getString)
            myContactsCursorLoader?.let { nonNullContactsCursor ->
                // 3. Теоретически мы можем получить таблицу, но без полей. Воспользуемся циклом while, чтобы пробежаться по ней
                // nonNullContactsCursor.moveToNext() - возвращается true/false. Т.е., если следующий элемент есть, мы продолжаем цикл
                while (nonNullContactsCursor.moveToNext()) {
                    // 2.1. Однако, мы не напрямую получаем String. Внутри getString нам снова нужно обратиться к курсору, чтобы он вернул адрес к ресурсу
                    val contactId = nonNullContactsCursor.getString(nonNullContactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val contactName = nonNullContactsCursor.getString(nonNullContactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                    // 4. И выведем логи для проверки
                    Log.d(TAG, "Contact ID: $contactId, contact name: $contactName")

                    // 5. Теперь получим номера телефонов. Они находятся в другой таблице, на другом уровне вложенности. Для того, чтобы найти нужные номера мы будем использовать id из предыдущей таблицы
                    // Но для начала нужно узнать, сколько у контакта телефонных номеров
                    val contactsNumberCount = nonNullContactsCursor.getInt(nonNullContactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                    // 6. Делаем проверку, есть ли у котнакта хоть один телефон
                    if (contactsNumberCount > 0) {
                        // 7. И снова получаем таблицу строк (с помощью CONTENT RESOLVER), на этот раз таблицу с телефонными номерами
                        // Номера контакта будем получать по его id из предыдущей таблицы
                        val myContactsNumberCursorLoader = requireActivity().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // Передаём uri ("дайте таблицу")
                            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER), // ("и верните только номера,")
                            "contact_id = $contactId", // ("которые соответствуют этой id)
                            null,
                            null
                        )

                        // 8. Если myContactsNumberCursorLoader(таблица) не null, пройдемся по ней и запишем номера в список
                        myContactsNumberCursorLoader?.let { nonNullNumberCursor ->
                            // Сформируем изменяемый список
                            val numbersList = mutableListOf<String>()
                            // Если таблица не пустая, получаем number (или несколько) и затем добавляем в список
                            while (nonNullNumberCursor.moveToNext()) {
                                val number = nonNullNumberCursor.getString(nonNullNumberCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                numbersList.add(number)
                            }
                            // 10. Полученный список номеров одного контакта проверяем на пустоту
                            if (numbersList.isNotEmpty()){
                                // И добавляем в список объектов класса ContactData (инициализирован в начале этого класса)
                                contactDataList.add(ContactData(contactName, numbersList))
                            }
                        }
                    }
                }
                // Итого в этом цикле while() мы пробегаемся по каждой строке, получаем id нового контакта и затем обрабатываем этот id в поисках номеров
                Log.d(TAG, "Contacts list size: ${contactDataList.size}")
                // 11. И показываем наши контакты в RecyclerView
                showContactsList()
            }

        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "caught an exception")
        }
    }

    // private функции - только для ContactsFragment:
    private fun showContactsList() {
        if (contactDataList.isNotEmpty()) {
            // 12. Чтобы передать список контактов в RecyclerView - нужно его создать и затем дописать метод
            contactsRecyclerView.adapter = ContactsRecycleAdapter(contactDataList)
        }
    }

    // отправляем данные в MainActivity, которое подписано на ключ NAVIGATION_EVENT
    private fun sendNavigationEvents() {
        requireActivity().supportFragmentManager.setFragmentResult( // Пишем requireActivity() вместо activity, т.к. activity м.б. null и нужна будет дополнительная проверка
            // Буду отправлять на слушателя с ключом NAVIGATION_EVENT (т.е. это ключ, по которому мы регистрировали слушателя ранее)
            MainActivity.NAVIGATION_EVENT,
            // В bundle указываемключ-значение данных, которые хотим передать
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "ContactsFragment created")
        )
    }
}

// 9. Создадим data класс для наших данных
data class ContactData(val name: String,val numbers: List<String>)