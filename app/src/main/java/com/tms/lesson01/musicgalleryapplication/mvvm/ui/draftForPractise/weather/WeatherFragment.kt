package com.tms.lesson01.musicgalleryapplication.mvvm.ui.draftForPractise.weather

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.WeatherWidget

/**
 * Фрагмент для отображения погоды (с помощью APi на получение погоды).
 * Из фрагмента мы будем вызывать view model, в которую будем передавать локацию для получения погоды по локации
 * (ранее делали виджет на получение погоды по городу).
 * Использовать будем RxJava (ранее использоввали корутины).
 *
 * WeatherServiceRx - наш сервис. Формирует запрос (ссылку после base url - end point)
 * WeatherNetwork - содержит base url, возвращает сервис WeatherServiceRx, создаёт ретрофит
 * WeatherViewModel - запрашивает погоду по локации из фрагмента, используя RxJava
 */
class WeatherFragment: Fragment() {

    companion object {
        private const val TAG = "WeatherFragment"
    }

    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var textWeatherCity: TextView
    private lateinit var textWeatherTemperature: TextView
    private lateinit var textWeatherHumidity: TextView
    private lateinit var imageWeather: ImageView
    private lateinit var textCounter: TextView


    // LOCATION -> 1.1. Переменная для нашего FusedLocationProviderClient. Именно он имеет в себе методы, с помощью которых мы можем определить локацию
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_draft_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textWeatherCity = view.findViewById(R.id.text_weatherCity)
        textWeatherTemperature = view.findViewById(R.id.text_weatherTemperature)
        textWeatherHumidity = view.findViewById(R.id.text_weatherHumidity)
        imageWeather = view.findViewById(R.id.image_weather)
        textCounter = view.findViewById(R.id.text_counter)

        // LOCATION -> 1.2. Получим наш FusedLocationProviderClient. Именно он имеет в себе методы, с помощью которых мы можем определить локацию
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // LOCATION -> 1.3. Создадим метод для получения Current location либо Last location
        getCurrentOrLastLocation()

        subscribeOnLiveDala()
        // For test RxObservables
        viewModel.testFlowable()
    }

    private fun subscribeOnLiveDala() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner){ weatherResponce ->
            // Возьмём тот де динамический текст, который мы писали для weather widget. И дополняем его позициями из полученного weatherResponce
            val cityText = requireContext().getString(R.string.widget_text_city, weatherResponce.name)
            val temperatureText = requireContext().getString(R.string.widget_text_temperature, weatherResponce.main.temp.toString())
            val humidityText = requireContext().getString(R.string.widget_text_humidity, weatherResponce.main.humidity.toString())
            // И присваиваем его нашим полям
            textWeatherCity.text = cityText
            textWeatherTemperature.text = temperatureText
            textWeatherHumidity.text = humidityText

            // Разберемся с картинкой
            try {
                // Ссылка
                val url =
                    "${WeatherWidget.ICON_BASE_URL}/${WeatherWidget.IMAGE_PATH}/${WeatherWidget.W_PATH}/${weatherResponce.weather.first().icon}${WeatherWidget.FORMAT_SUFFIX}"
                // Загружаем её и вставляем в image view
                Glide
                    .with(requireContext())
                    .asBitmap()
                    .load(url)
                    .into(imageWeather)
            } catch (e: Exception) {
                Log.e(WeatherWidget.TAG, e.message ?: "Error during loading weather icon by url")
            }

            // For test RxObservables
            viewModel.counterLiveData.observe(viewLifecycleOwner){ counter ->
                textCounter.text = counter.toString()
            }
        }
    }

    // LOCATION -> 1.4. Создадим метод для получения Current location либо Last location
    // @SuppressLint("MissingPermission") - эта аннотация означает, что мы должны точно знать, что мы уже проверили, есть ли у нас PERMISSION
    // Т.к. на этот фрагмент мы попадаем только при соглачии на получение местоположения, значит здесь разрешение у нас точно есть
    // Используем инициализированный fusedLocationProviderClient для доступа к методам
    @SuppressLint("MissingPermission")
    private fun getCurrentOrLastLocation() {
        // Last location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location
                ?: return@addOnSuccessListener // Т.к. location может прилететь null, пишем тернарный оператор. Если location = null, то просто выходим из метода addOnSuccessListener
            Log.d(
                TAG,
                "LastLocation: latitude = ${location.latitude}, longitude = ${location.longitude}"
            ) // Проверяем получение ширины и долготы в логе

            getWeather(location = location)
        }

        // Current location
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                // Создадим переменную со значением по умолчанию
                private var isCancellationRequested = false

                // Если запрошено
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    isCancellationRequested = true
                    return this
                }

                // Проверка статуса
                override fun isCancellationRequested(): Boolean {
                    return isCancellationRequested
                }
            }).addOnSuccessListener { location: Location? ->
            // Т.к. location может прилететь null, пишем тернарный оператор. Если location = null, то просто выходим из метода addOnSuccessListener
            location ?: return@addOnSuccessListener
            // Проверяем получение ширины и долготы в логе
            Log.d(TAG, "LastLocation: latitude = ${location.latitude}, longitude = ${location.longitude}")

            getWeather(location = location)
        }
    }

    private fun getWeather(location: Location) {
        viewModel.getWeather(location)
    }

}