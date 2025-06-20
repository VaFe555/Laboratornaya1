package com.example.weatherapp_lab1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var cityEditText: EditText
    private lateinit var fetchWeatherButton: Button
    private lateinit var cityTextView: TextView
    private lateinit var weatherIconImageView: ImageView
    private lateinit var temperatureTextView: TextView
    private lateinit var pressureTextView: TextView
    private lateinit var errorTextView: TextView
    private lateinit var unitsRadioGroup: RadioGroup
    private lateinit var celsiusRadioButton: RadioButton
    private lateinit var fahrenheitRadioButton: RadioButton

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityEditText = findViewById(R.id.cityEditText)
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton)
        cityTextView = findViewById(R.id.cityTextView)
        weatherIconImageView = findViewById(R.id.weatherIconImageView)
        temperatureTextView = findViewById(R.id.temperatureTextView)
        pressureTextView = findViewById(R.id.pressureTextView)
        errorTextView = findViewById(R.id.errorTextView)
        unitsRadioGroup = findViewById(R.id.unitsRadioGroup)
        celsiusRadioButton = findViewById(R.id.celsiusRadioButton)
        fahrenheitRadioButton = findViewById(R.id.fahrenheitRadioButton)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        fetchWeatherButton.setOnClickListener {
            val city = cityEditText.text.toString()
            val units = if (celsiusRadioButton.isChecked) "metric" else "imperial"
            viewModel.fetchWeather(city, units)
        }

        viewModel.weatherData.observe(this) { weather ->
            cityTextView.text = "Город: ${weather.name}"
            temperatureTextView.text = "Температура: ${weather.main.temp}°"
            pressureTextView.text = "Давление: ${weather.main.pressure} hPa"

            val iconCode = weather.weather[0].icon
            val iconUrl = "https://openweathermap.org/img/w/$iconCode.png"
            Glide.with(this)
                .load(iconUrl)
                .into(weatherIconImageView)

            errorTextView.visibility = TextView.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                errorTextView.text = "Ошибка: $message"
                errorTextView.visibility = TextView.VISIBLE
            } else {
                errorTextView.visibility = TextView.GONE
            }
        }
    }
}