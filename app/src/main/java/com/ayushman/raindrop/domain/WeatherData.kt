package com.ayushman.raindrop.domain

data class WeatherData(
    val countryName: String,
    val cityName : String,
    val weatherId : Int,
    val weatherDesc : String,
    val temperature : Double
)
