package com.example.weatherapp_lab1

data class Weather(
    val main: Main,
    val weather: List<WeatherDescription>,
    val wind: Wind,
    val clouds: Clouds,
    val name: String
)