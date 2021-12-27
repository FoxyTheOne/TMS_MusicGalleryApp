package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.Playlist

// Для реакции по клику на элемент списка, передадим в конструктор анонимную функцию. Затем отдаём эту лямбду каждому ViewHolder, чтобы слушать, на что кликнул пользователь
class PlaylistsRecyclerAdapter(private val playlists: List<Playlist>, private val selectedItem: (Playlist) -> Unit):
    RecyclerView.Adapter<PlaylistsRecyclerAdapter.PlaylistViewHolder>() {

    // Создаём элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_playlist_item, parent,false)
        return PlaylistViewHolder(view, selectedItem)
    }

    // Сюда залетает элемент списка, к-рый был создан в onCreateViewHolder() и здесь мы его наполняем
    // Однако, лучше просто вызвать метод из вложенного класа, где и осуществить непосредственно наполнение, описание clickListener и проч.
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.setPlaylist(playlists[position])
    }

    // Возвращает количество элементов списка
    override fun getItemCount(): Int = playlists.size

    class PlaylistViewHolder (private val view: View, private val selectedItem: (Playlist) -> Unit):
        RecyclerView.ViewHolder(view) {

        private var playlist: Playlist? = null

        init {
            view.setOnClickListener { // Отлавливаем клики по экрану
                playlist?.let{ // Если клик по альбому - проверка на null
                    selectedItem(it) // Если не null, возвращаем альбом
                }
            }
        }

        fun setPlaylist(playlist: Playlist) {
            this.playlist = playlist

            val imageView = view.findViewById<AppCompatImageView>(R.id.playlist_logo)
//            Если картинка из интернета:
//            Glide.with(view.context).load(playlist.imageID).into(imageView)
            val imageID = playlist.imageID
            Glide.with(view.context).load(imageID).into(imageView)

            view.findViewById<AppCompatTextView>(R.id.playlist_name).text = playlist.name
        }

    }

}