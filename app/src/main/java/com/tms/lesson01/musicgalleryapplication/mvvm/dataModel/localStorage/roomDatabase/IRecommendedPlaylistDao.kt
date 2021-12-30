package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase

import androidx.room.*
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.RecommendedPlaylist
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject.YourFavouritesPlaylist

/**
 * 2. Уровень Dao нашей таблицы RecommendedPlaylist
 */
@Dao
interface IRecommendedPlaylistDao {
    // Метод для сохранения абсолютно нового(вых) плейлиста(тов). Используем REPLACE, чтобы плейлист сохранялся/обновлялся в любом случае, даже если он уже есть в базе
    // ABORT - если вы хотите, чтобы транзакция остановилась при первом нарушении (н-р, третий плейлист в списке не новый) и откатила изменения, внесенные оператором, вызвавшим нарушение
    // IGNORE - транзакция продолжится со слеующего оператора, если какой-либо из операторов нарушает ограничение, просто игнорируя и фактически не выполняя этот проблемный оператор
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecommendedPlaylist(recommendedPlaylist: RecommendedPlaylist)

    @Insert
    fun insertRecommendedPlaylists(vararg recommendedPlaylist: RecommendedPlaylist)

    // Удаляем 1 альбом
    @Delete
    fun deleteRecommendedPlaylist(recommendedPlaylist: RecommendedPlaylist)

    // Удаляем тот список, который передадим (весь или, н-р, 3 из 10)
    @Delete
    fun deleteRecommendedPlaylists(vararg recommendedPlaylist: RecommendedPlaylist)

    // Удаляем все альбомы
    // Query - запрос. Т.е. мы помечаем, что это будет неш кастомный запрос
    @Query("DELETE FROM RecommendedPlaylist")
    fun deleteRecommendedPlaylists()

    // Update - Обновляем существующий
    // Используем, только если обновляем. Если сохраняем информацию из back-end, то этот метод НЕ подходит
    // Н-р, закеширован наш профайл. Но изменили ник. Можно сделать одновременно запрос на обновление + параллельно в кеше
    @Update
    fun updateRecommendedPlaylist(recommendedPlaylist: RecommendedPlaylist)

    // Ищем опред-е значение: ключевое слово SELECT, все значения (*) из (FROM) RecommendedPlaylist, у которых (WHERE) name такой же, как (LIKE:) recommendedPlaylistName
    @Query("SELECT * FROM RecommendedPlaylist WHERE name LIKE:recommendedPlaylistName")
    fun searchRecommendedPlaylists(recommendedPlaylistName: String): List<RecommendedPlaylist>
}