package com.tms.lesson01.musicgalleryapplication.mvvm.ui.countries

class CountriesModel: ICountriesModel {
    override fun getCountries(): List<String> = listOf("Belarus", "Ukraine", "Poland")
}