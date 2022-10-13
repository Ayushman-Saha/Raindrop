package com.ayushman.raindrop.database.weatherData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayushman.raindrop.domain.WeatherData

@Entity(tableName = "weather_data")
data class DatabaseWeatherData(

    @PrimaryKey(autoGenerate = false)
    val id : Int = 69,

    @ColumnInfo(name = "city_name")
    val cityName : String,

    @ColumnInfo(name = "country_name")
    val countryName: String,

    @ColumnInfo(name = "weather_id")
    val weatherId : Int,

    @ColumnInfo(name = "weather_description")
    val weatherDesc : String,

    @ColumnInfo(name = "temperature")
    val temperature : Double
)

fun DatabaseWeatherData.asDomainModel() : WeatherData {
   return WeatherData(
        cityName = cityName,
        countryName = countryName,
        weatherId = weatherId,
        weatherDesc = weatherDesc,
        temperature = temperature
    )
}