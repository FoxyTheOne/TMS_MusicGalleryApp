package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feels_like: Double,
    @SerializedName("temp_min") val temp_min: String,
    @SerializedName("temp_max") val temp_max: String,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val sea_level: Int,
    @SerializedName("grnd_level") val grnd_level: Int
)