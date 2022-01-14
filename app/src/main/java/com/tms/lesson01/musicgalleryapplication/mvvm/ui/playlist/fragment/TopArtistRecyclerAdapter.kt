package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment

import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.artist.Artist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tms.lesson01.musicgalleryapplication.R

// Для реакции по клику на элемент списка, передадим в конструктор анонимную функцию. Затем отдаём эту лямбду каждому ViewHolder, чтобы слушать, на что кликнул пользователь
class TopArtistRecyclerAdapter(private val topArtists: List<Artist>, private val selectedItem: (Artist) -> Unit):
    RecyclerView.Adapter<TopArtistRecyclerAdapter.TopArtistViewHolder>() {

    // Создаём элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_playlists_artist_item, parent,false)
        return TopArtistViewHolder(view, selectedItem)
    }

    // Сюда залетает элемент списка, к-рый был создан в onCreateViewHolder() и здесь мы его наполняем
    // Однако, лучше просто вызвать метод из вложенного класа, где и осуществить непосредственно наполнение, описание clickListener и проч.
    override fun onBindViewHolder(holder: TopArtistViewHolder, position: Int) {
        holder.setArtist(topArtists[position])
    }

    // Возвращает количество элементов списка
    override fun getItemCount(): Int = topArtists.size

    class TopArtistViewHolder(private val view: View, private val selectedItem: (Artist) -> Unit):
        RecyclerView.ViewHolder(view) {

        private val imageView: AppCompatImageView = view.findViewById(R.id.playlist_logo)
        private val title: AppCompatTextView = view.findViewById(R.id.playlist_name)
        private var artist: Artist? = null

        // При клике на item будем возвращать artist
        init {
            view.setOnClickListener { // Отлавливаем клики по экрану
                artist?.let{ // Если клик по artist - проверка на null
                    selectedItem(it) // Если не null, возвращаем artist
                }
            }
        }

        fun setArtist(artist: Artist) {
            Glide.with(view.context)
                .load(artist.image.last().text) // last() потому, что image у нас прилетает в нескольких расширениях и последний будет с самым высоким расширением. text - это наша ссылка на картинку
                .into(imageView)

            title.text = artist.name
        }
    }
}