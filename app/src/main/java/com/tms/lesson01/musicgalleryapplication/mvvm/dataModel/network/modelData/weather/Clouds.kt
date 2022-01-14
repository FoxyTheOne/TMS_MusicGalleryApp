package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val all: Int
)


