package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.modelData.weather.WeatherResponse
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.WeatherWidget

/**
 * Элемент погоды, который прилетит в фрагмент, мы будем просто добавлять в RecyclerView.
 * Соответственно RecyclerView будет обновляться при получаении нового элемента списка.
 * Так же обработаем клик по элементу списка
 */
// 1.1. ОБРАБОТКА КЛИКА -> Передадим анонимную функцию через конструктор
class WeatherRecyclerAdapter(private val onItemClicked: (WeatherResponse) -> Unit): RecyclerView.Adapter<WeatherRecyclerAdapter.WeatherViewHolder>() {

    companion object {
        private const val TAG = "WeatherRecyclerAdapter"
    }

    // Создаём список погоды здесь, т.к. не передавали его через конструктор.
    private val weatherResultList = mutableListOf<WeatherResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_draft_weather_item, parent, false)
        // 1.3. ОБРАБОТКА КЛИКА -> Т.к. у нас несколько ViewHolder, нужно каждый раз передавать в него нашу анонимную функцию
        return WeatherViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.setWeather(weatherResultList[position])
    }

    override fun getItemCount(): Int = weatherResultList.size

    // Список погод мы создавали здесь, поэтому создадим метод для обновления этого списка, если прилетит новая погода
    fun addAllWeathers(weatherResultList: MutableList<WeatherResponse>) {
        this.weatherResultList.clear()
        this.weatherResultList.addAll(weatherResultList) // Очищаем старый список и записываем в него передаваемые в метод значения
        notifyDataSetChanged()
    }

    // 1.2. ОБРАБОТКА КЛИКА -> Передаём эту же функцию во view holder
    inner class WeatherViewHolder(private val view: View, private val onItemClicked: (WeatherResponse) -> Unit): RecyclerView.ViewHolder(view) {

        // 1.4. ОБРАБОТКА КЛИКА -> Будем просто возвращать элемент, на который кликнули. Создадим переменную
        private var weather: WeatherResponse? = null

        private val textWeatherCity: TextView = view.findViewById(R.id.textRecycler_weatherCity)
        private val textWeatherTemperature: TextView = view.findViewById(R.id.textRecycler_weatherTemperature)
        private val textWeatherHumidity: TextView = view.findViewById(R.id.textRecycler_weatherHumidity)
        private val imageWeather: ImageView = view.findViewById(R.id.imageRecycler_weather)

        // 1.6. ОБРАБОТКА КЛИКА -> В функции init{} д.б. view.setOnClickListener{}, который передаст информацию в фрагмент, а уже из фрагмента мы будем передавать информацию во view model
        // Такая передача информации осуществляется с помощью анонимных функций. Она передаётся либо через конструктор, либо через методы. Мы воспользуемся конструктором
        init {
            view.setOnClickListener {
                // 1.6. ОБРАБОТКА КЛИКА -> Передадим по клику нашу переменную, если она не null (передаём в нашу анонимную функцию)
                weather?.let{ nonNullWeather ->
                    onItemClicked(nonNullWeather)
                }
            }
        }

        fun setWeather(weather: WeatherResponse) {
            // 1.5. ОБРАБОТКА КЛИКА -> В методе обработки элемента списка, инициализируем нашу переменную
            this.weather = weather

            itemView.context?.let { context ->
                val setTextWeatherCity = context.getString(R.string.widget_text_city, weather.name)
                val setTextWeatherTemperature = context.getString(R.string.widget_text_temperature, weather.main.temp.toString())
                val setTextWeatherHumidity = context.getString(R.string.widget_text_humidity, weather.main.humidity.toString())

                textWeatherCity.text = setTextWeatherCity
                textWeatherTemperature.text = setTextWeatherTemperature
                textWeatherHumidity.text = setTextWeatherHumidity

                try {
                    // Ссылка
                    val url =
                        "${WeatherWidget.ICON_BASE_URL}/${WeatherWidget.IMAGE_PATH}/${WeatherWidget.W_PATH}/${weather.weather.first().icon}${WeatherWidget.FORMAT_SUFFIX}"
                    // Загружаем её и вставляем в image view
                    Glide
                        .with(context)
                        .asBitmap()
                        .load(url)
                        .into(imageWeather)
                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "Error during loading weather icon by url")
                }
            }

        }

    }

}