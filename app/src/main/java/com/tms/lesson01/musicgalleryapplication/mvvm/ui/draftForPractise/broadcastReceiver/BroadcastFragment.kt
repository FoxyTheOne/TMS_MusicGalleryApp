package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.tms.lesson01.musicgalleryapplication.R
import kotlinx.parcelize.Parcelize

class BroadcastFragment: Fragment() {
    companion object {
        private const val TAG = "BroadcastFragment"
        // Фильтр для нашего Broadcast
        private const val BROADCAST_UPDATE_CONTENT = "BROADCAST_UPDATE_CONTENT"
        // Фильтры для контента:
        private const val BROADCAST_TEXT = "BROADCAST_TEXT"
        private const val BROADCAST_OBJECT_BUNDLE = "BROADCAST_OBJECT_BUNDLE"
        private const val BROADCAST_OBJECT = "BROADCAST_OBJECT"
    }

    // Переменные класса
    private lateinit var buttonSendBroadcast: AppCompatButton
    private lateinit var textBroadcastContext: AppCompatTextView
    private lateinit var textBroadcastContext2: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_draft_broadcast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Находим наши view
        buttonSendBroadcast = view.findViewById(R.id.button_sendBroadcast)
        textBroadcastContext = view.findViewById(R.id.text_broadcastContext)
        textBroadcastContext2 = view.findViewById(R.id.text_broadcastContext2)

        // SEND SIMPLE STRING -> 1.3. Зарегистрируем наш BroadcastReceiver и его intent фильтр
        requireActivity().registerReceiver(MyBroadcastReceiver(), IntentFilter(BROADCAST_UPDATE_CONTENT))

        // SEND SIMPLE STRING -> 1.4. Вешаем .setOnClickListener на нашу кнопочку
        buttonSendBroadcast.setOnClickListener {
            // SEND SIMPLE STRING -> 1.5. Обращаемся к активити и отправляем бродкаст (intent с донастройкой - наш фильтр + строка, которую хотим отобразить (ч/з putExstra, т.е. ключ и что хотим отправить))
            requireActivity().sendBroadcast(Intent().apply {
                action = BROADCAST_UPDATE_CONTENT
                putExtra(BROADCAST_TEXT, "Broadcast successfully received.")
                // Передавать можно много значений (для этого передаём несколько putExtra())
                // !!! Так же можно передавать не только примитивы. Это могут быть только объекты, к-рые либо Serialized, либо Parcelable !!!
                // Но в таком случае мы передаём их через Bundle, putParcelable()
                // SEND OBJECT -> 2.3. Передадим объект через бродкаст
                putExtra(BROADCAST_OBJECT_BUNDLE, Bundle().apply {
                    putParcelable(BROADCAST_OBJECT, User("Vasil", "Bykov", 24))
                })
            })
        }
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // SEND SIMPLE STRING -> Если сообщение успешно получено бродкастом, выведем сообщение в нашем TextView
            // SEND SIMPLE STRING -> 1.1. Для этого обратимся к intent (намерение) и достанем необходимую строку по intent фильтру (фильтр инициализируем, как константу, в начале класса)
            val updatedTextOnButton = intent?.getStringExtra(BROADCAST_TEXT) ?: "Got some trouble"
            // SEND SIMPLE STRING -> 1.2. Присваиваем новый текст нашему TextView
            textBroadcastContext.text = updatedTextOnButton

            // SEND OBJECT -> 2.4 Теперь получим не String, объект
            val userBundle = intent?.getBundleExtra(BROADCAST_OBJECT_BUNDLE)
            val user = userBundle?.getParcelable<User>(BROADCAST_OBJECT)
            // SEND OBJECT -> 2.5. Присваиваем новый текст нашему TextView
            user?.let { nonNullUser ->
                val userText = "Name: ${nonNullUser.name}, surname: ${nonNullUser.surname}, age: ${nonNullUser.age}"
                textBroadcastContext2.text = userText
            }
        }
    }
}

// SEND OBJECT -> 2.1. Чтобы создавать @Parcelize объекты, необходимо имплементировать плагин
// SEND OBJECT -> 2.2. Для примера передачи объектов через BroadcastReceiver, создадим класс User
@Parcelize
data class User(val name: String, val surname: String, val age: Int): Parcelable