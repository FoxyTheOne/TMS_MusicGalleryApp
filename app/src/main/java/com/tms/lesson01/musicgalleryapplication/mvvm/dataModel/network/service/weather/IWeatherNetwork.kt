package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather

/**
 * Код из проекта преподавателя (для виджета погоды)
 * 3. Интерфейс, чтобы давать доступ к сервисам
 */
interface IWeatherNetwork {
    // Ф-ция, которая возвращает нам сервис
    fun getWeatherServiceCoroutine(): WeatherServiceCoroutine
    fun getWeatherServiceRx(): WeatherServiceRx
}
