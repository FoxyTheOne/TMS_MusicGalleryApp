package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.otherApplicationComponent.contenctProvider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tms.lesson01.musicgalleryapplication.R

/**
 * 1. В конструкторе указываем, что будем передавать в адаптер
 * 2. Наследуемся от RecyclerView.Adapter<тип - наш вложенный класс>()
 * 3. Создаём вложенный класс - в конструктор передаём view, наследуемся от RecyclerView.ViewHolder(view)
 */
class ContactsRecycleAdapter(private val contactsList: List<ContactData>): RecyclerView.Adapter<ContactsRecycleAdapter.ContactsViewHolder>() {
    // Создаём элемент списка. Возвращаем элемент вложенного класса: ViewHolder. В конструктор передаём view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_draft_contacts_item, parent,false)
        return ContactsViewHolder(view)
    }

    // Сюда залетает элемент списка, к-рый был создан в onCreateViewHolder() и здесь мы его наполняем
    // Однако, лучше просто вызвать метод из вложенного класа, где и осуществить непосредственно наполнение, описание clickListener и проч.
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.setContact(contactsList[position])
    }

    // Размер списка
    override fun getItemCount(): Int = contactsList.size

    inner class ContactsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contactInfoText = view.findViewById<AppCompatTextView>(R.id.text_contactInfo)

        // 4. Создадим метод, который будет принимать в себя каждый элемент списка List<ContactData>, по одному, и отображать его
        fun setContact(contact: ContactData) {
            // Составляем строку для отображения. Из списка номеров возьмём самый первый
            val textToShow = "${contact.name} ${contact.numbers.first()}"
            // И вставляем текст в место для текста у нашего элемента
            contactInfoText.text = textToShow
        }
    }
}