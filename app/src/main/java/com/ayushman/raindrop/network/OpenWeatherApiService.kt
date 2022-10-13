package com.ayushman.raindrop.network

import com.ayushman.raindrop.OPEN_WEATHER_KEY
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//The base url for spotify API
private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

//The moshi adapter for parsing JSON
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface OpenWeatherApiService {
    @GET("weather")
    fun getWeatherDataAsync(@Query("q") city: String,
                            @Query("units") unit: String ="metric",
                            @Query("appid") appid: String = OPEN_WEATHER_KEY
    ) : Deferred<NetworkWeatherData>

}

//Exposing the services using object
object OpenWeatherApi {
    //The retrofit object
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val openWeatherService: OpenWeatherApiService =
        retrofit.create(OpenWeatherApiService::class.java)
}
