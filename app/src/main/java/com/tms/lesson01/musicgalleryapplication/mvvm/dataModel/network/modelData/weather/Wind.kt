package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int,
    @SerializedName("gust") val gust: Double
)