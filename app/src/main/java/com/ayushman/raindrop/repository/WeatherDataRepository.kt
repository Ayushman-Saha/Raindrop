package com.ayushman.raindrop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.weatherData.asDomainModel
import com.ayushman.raindrop.domain.WeatherData
import com.ayushman.raindrop.network.OpenWeatherApi
import com.ayushman.raindrop.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class WeatherDataRepository (private val localDatabase: LocalDatabase) {

    val weatherData : LiveData<WeatherData> = Transformations.map(localDatabase.weatherDataDao.getWeatherDataObservable()) {
        it?.asDomainModel()
    }

    suspend fun refreshWeatherData(cityName : String) {
        withContext(Dispatchers.IO) {
            try {
                val weatherData = OpenWeatherApi.openWeatherService.getWeatherDataAsync(city = cityName).await()
                localDatabase.weatherDataDao.insert(weatherData.asDatabaseModel())
                Timber.i(weatherData.toString())
            } catch (e: Exception) {
                Timber.e(e.message)
            }
        }
    }

}