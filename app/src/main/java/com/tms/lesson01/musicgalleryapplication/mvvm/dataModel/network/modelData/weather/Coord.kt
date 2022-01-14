package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather

import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)