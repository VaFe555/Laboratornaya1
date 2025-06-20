package com.example.weatherapp_lab1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<Weather>()
    val weatherData: MutableLiveData<Weather> get() = _weatherData
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    private val apiKey = "f05ed0d81c1bcc83546ab9be71e4508f"
    private val baseUrl = "https://api.openweathermap.org/data/2.5/"

    private val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    fun fetchWeather(city: String, units: String = "metric") {
        viewModelScope.launch {
            try {
                val response = weatherApiService.getWeather(city, apiKey, units)
                if (response.isSuccessful) {
                    _weatherData.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Ошибка при получении данных о погоде"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Проблемы с соединением: ${e.message}"
            }
        }
    }
}