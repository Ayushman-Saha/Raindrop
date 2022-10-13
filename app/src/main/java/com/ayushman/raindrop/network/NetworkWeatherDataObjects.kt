package com.ayushman.raindrop.network

import com.ayushman.raindrop.database.weatherData.DatabaseWeatherData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkWeatherData (

    @Json(name = "name")
    val cityName : String,

    @Json(name = "sys")
    val countryName : CountryName,

    @Json(name = "weather")
    val weather: List<WeatherInfo>,

    @Json(name = "main")
    val temperature : TemperatureData

    )

@JsonClass(generateAdapter = true)
data class CountryName (
    val country : String
    )

@JsonClass(generateAdapter = true)
data class WeatherInfo(
    val id : Int,
    val description : String
)

@JsonClass(generateAdapter = true)
data class TemperatureData(
    val temp : Double
)

fun NetworkWeatherData.asDatabaseModel() : DatabaseWeatherData {
    return DatabaseWeatherData(
        countryName = countryName.country,
        cityName = cityName,
        weatherId = weather[0].id,
        weatherDesc = weather[0].description,
        temperature = temperature.temp
        )
}

