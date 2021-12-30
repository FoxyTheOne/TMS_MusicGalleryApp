package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network

import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.RecommendedPlaylist
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.YourFavouritesPlaylist

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для работы с запросами на сервер о музыке имеем отдельный класс. Обработка данных и возвращение результата
 * hw02. 2. Методы пишем на основе OCP - Принцип открытости/закрытости. Программные сущности (классы, модули, ф-ции и проч.) должны быть открыты для расширения, но закрыты для изменений
 * hw02. 3. ISP - Принцип разделения интерфейса. Наследуемся от более мелкого, специфического интерфейса
 * hw02. 4. LSP - Принцип подстановки Барбары Лисков. Подклассы должны переопределять методы базового класса так, чтобы не нарушалась функциональность с точки зрения клиента
 */
class NetworkMusicServiceModel : INetworkMusicService {
    private val yourFavorites = listOf(
        // Playlist("Wake up Songs", ContextCompat.getDrawable(context, R.drawable.wake_up_songs)),
        // Playlist("Wake up Songs", setImageResource(R.drawable.wake_up_songs)),

        // int resourceId = R.mipmap.ic_launcher;

        YourFavouritesPlaylist("Wake up Songs", R.drawable.wake_up_songs),
        YourFavouritesPlaylist("Classic", R.drawable.classic),
        YourFavouritesPlaylist("Guitar solo songs", R.drawable.guitar_solo_songs1)

//        Playlist("Wake up Songs", "https://lastfm.freetls.fastly.net/i/u/174s/21f77cd622ec3ab6e29f13b4a48c9e9d.png"),
//        Playlist("Classic", "https://lastfm.freetls.fastly.net/i/u/174s/21f77cd622ec3ab6e29f13b4a48c9e9d.png"),
//        Playlist("Guitar solo songs", "https://lastfm.freetls.fastly.net/i/u/174s/21f77cd622ec3ab6e29f13b4a48c9e9d.png")
    )
    private val recommendedPlaylists = listOf(
        RecommendedPlaylist("Indie rock", R.drawable.indie_rock),
        RecommendedPlaylist("Acoustics", R.drawable.acoustics),
        RecommendedPlaylist("Drum & Bass", R.drawable.drum_n_bass1)

//        Playlist("Indie rock", "https://lastfm.freetls.fastly.net/i/u/174s/21f77cd622ec3ab6e29f13b4a48c9e9d.png"),
//        Playlist("Acoustics", "https://lastfm.freetls.fastly.net/i/u/174s/21f77cd622ec3ab6e29f13b4a48c9e9d.png"),
//        Playlist("Drum & Bass", "https://lastfm.freetls.fastly.net/i/u/174s/21f77cd622ec3ab6e29f13b4a48c9e9d.png")
    )

    override fun getFavouriteMusic(): List<Any> {
        TODO("Not yet implemented")
    }

    override fun getYourFavorites(): List<YourFavouritesPlaylist> = yourFavorites

    override fun getRecommendedPlaylists(): List<RecommendedPlaylist> = recommendedPlaylists

    override fun updateFavouriteMusic(data: Any) {
//        TODO("Not yet implemented")
    }
}
