package com.tms.lesson01.musicgalleryapplication.mvvm.utils

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather.WeatherResponse
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.weather.WeatherNetwork
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.success.activityOld.SuccessLoginActivity
import kotlinx.coroutines.*

/**
 * Implementation of App Widget functionality.
 * Код из проекта преподавателя
 *
 * Обновление виджета программно настраивается в MainActivity
 */
class WeatherWidget : AppWidgetProvider() {
    companion object {
        const val TAG = "WeatherWidget"
        const val ICON_BASE_URL = "https://openweathermap.org"
        const val IMAGE_PATH = "img"
        const val W_PATH = "w"
        const val FORMAT_SUFFIX = ".png"
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    // Когда обновляется (пока что раз в час), сюда залетает каждый элемент виджета по очереди и обрабатывается
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        System.currentTimeMillis()
        serviceScope.launch(Dispatchers.IO) {
            // onUpdate -> 2.1. После создания (!!!) RETROFIT и INTERCEPTOR, обращаемся к экземпляру нашего синглтона WeatherNetwork через .getInstance()
            // и получаем интерфейс для взаимодействия
            val weather = WeatherNetwork.getInstance()
            // onUpdate -> 2.2. И затем вызываем нужные методы, обращаясь к сервису через интерфейс
            val response = weather.getWeatherService().getWeather()

            serviceScope.launch(Dispatchers.Main) {
                // There may be multiple widgets active, so update all of them
                for (appWidgetId in appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, response)
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        // onDisabled -> 3. Закрыть поток
        serviceScope.cancel()
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    /**
     * Пример метода updateAppWidget, написаного НЕ на занятии
     */
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        weather: WeatherResponse
    ) {
        // CUSTOM WIDGET -> 1. Пишем текст, который хотим отобразить, находим картинки для отображения
        val textSuccessButton = context.getString(R.string.playlists_buttonGoToSuccess)
        val textAppButton = context.getString(R.string.open_app)
        val textCity = context.resources.getString(R.string.widget_text_city, weather.name)
        val textTemperature =
            context.getString(R.string.widget_text_temperature, weather.main.temp.toString())
        val textHumidity =
            context.getString(R.string.widget_text_humidity, weather.main.humidity.toString())

        // CUSTOM WIDGET -> 2. Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.layout_widget_weather)

        // CUSTOM WIDGET -> 3. Описываем деёствия, которые потом хотим назначить (отложенные намерения, pending intent)
        val pendingOpenSuccess = PendingIntent.getActivity(
            context,
            101,
            Intent(context, SuccessLoginActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pendingOpenApp = PendingIntent.getActivity(
            context,
            102,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // CUSTOM WIDGET -> 4. Назначаем текст и картинки
        views.setTextViewText(R.id.button_openSuccess, textSuccessButton)
        views.setTextViewText(R.id.button_openApp, textAppButton)

        views.setTextViewText(R.id.text_city, textCity)
        views.setTextViewText(R.id.text_temperature, textTemperature)
        views.setTextViewText(R.id.text_humidity, textHumidity)

        // На сколько я поняла, здесь реализована загрузка картинки для виджета (в корутине)
        serviceScope.launch(Dispatchers.IO) {
            try {
                val appWidgetTarget =
                    AppWidgetTarget(context, R.id.image_weather, views, appWidgetId)
                val url =
                    "$ICON_BASE_URL/$IMAGE_PATH/$W_PATH/${weather.weather.first().icon}$FORMAT_SUFFIX"

                Glide
                    .with(context)
                    .asBitmap()
                    .load(url)
                    .into(appWidgetTarget)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Error during load weather icon by url")
            }
        }

        // CUSTOM WIDGET -> 5. Назначаем действия (view, отложенное намерение)
        views.setOnClickPendingIntent(R.id.button_openSuccess, pendingOpenSuccess)
        views.setOnClickPendingIntent(R.id.button_openApp, pendingOpenApp)

        // CUSTOM WIDGET -> 6. Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    /**
     * Пример метода updateAppWidget, написано на занятии
     */
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // CUSTOM WIDGET -> 1. Пишем текст, который хотим отобразить, находим картинки для отображения
        val textSuccessButton = context.getString(R.string.playlists_buttonGoToSuccess)
        val textAppButton = context.getString(R.string.open_app)

        // CUSTOM WIDGET -> 2. Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.layout_widget_weather)

        // CUSTOM WIDGET -> 3. Описываем деёствия, которые потом хотим назначить (отложенные намерения, pending intent)
        val pendingOpenSuccess = PendingIntent.getActivity(
            context,
            101,
            Intent(context, SuccessLoginActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pendingOpenApp = PendingIntent.getActivity(
            context,
            102,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // CUSTOM WIDGET -> 4. Назначаем текст и картинки
        views.setTextViewText(R.id.button_openSuccess, textSuccessButton)
        views.setTextViewText(R.id.button_openApp, textAppButton)

        // CUSTOM WIDGET -> 5. Назначаем действия (view, отложенное намерение)
        views.setOnClickPendingIntent(R.id.button_openSuccess, pendingOpenSuccess)
        views.setOnClickPendingIntent(R.id.button_openApp, pendingOpenApp)

        // CUSTOM WIDGET -> 6. Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
