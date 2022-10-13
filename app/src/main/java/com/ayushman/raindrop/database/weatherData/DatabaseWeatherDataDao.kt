package com.ayushman.raindrop.database.weatherData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(databaseWeatherData: DatabaseWeatherData)

    @Query("SELECT * FROM weather_data WHERE id = 69")
    suspend fun get(): DatabaseWeatherData?

    @Query("SELECT * FROM weather_data WHERE id = 69")
    fun getWeatherDataObservable(): LiveData<DatabaseWeatherData>

    @Query(value = "DELETE FROM weather_data")
    fun clear()
}