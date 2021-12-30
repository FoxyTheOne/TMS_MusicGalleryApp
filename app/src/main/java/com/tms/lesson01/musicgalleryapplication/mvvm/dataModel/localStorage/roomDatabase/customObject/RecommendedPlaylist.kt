package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.roomDatabase.customObject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 1. Наша таблица 2
 */
@Entity
class RecommendedPlaylist (
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "imageID")
    val imageID: Int
)