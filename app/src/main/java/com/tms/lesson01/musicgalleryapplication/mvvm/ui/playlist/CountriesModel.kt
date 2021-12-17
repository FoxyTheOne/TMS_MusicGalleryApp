package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist

class CountriesModel: ICountriesModel {
    override fun getCountries(): List<String> = listOf("Belarus", "Ukraine", "Poland")
}