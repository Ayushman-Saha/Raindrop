@file:Suppress("UNCHECKED_CAST")

package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.repository.UserDataRepository
import com.ayushman.raindrop.repository.WeatherDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentWeatherHomeViewModel(context : Context) : ViewModel() {

    private val localDatabase = LocalDatabase.getInstance(context)

    private val weatherDataRepository = WeatherDataRepository(localDatabase = localDatabase)
    private val userDataRepository  = UserDataRepository(localDatabase = localDatabase)

    val weatherData = weatherDataRepository.weatherData
    val userData = userDataRepository.userData

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userData = localDatabase.userDataDao.get()
                userData?.let {
                    weatherDataRepository.refreshWeatherData(it.city)
                }
            }
        }
    }

    fun changeLocation(newCity : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDataRepository.refreshUserData(newCity)
                weatherDataRepository.refreshWeatherData(newCity)
            }
        }
    }

    fun clearPreference() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                localDatabase.userPreferenceDao.clear()
            }
        }
    }

    class FragmentWeatherViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentWeatherHomeViewModel(context) as T
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
