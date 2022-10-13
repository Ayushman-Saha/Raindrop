package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ayushman.raindrop.database.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentAdjustParamsViewModel(context: Context) : ViewModel() {

    //Getting the instance of the localDatabase
    private val localDatabase = LocalDatabase.getInstance(context)

    fun addParamsToPreferences(
        acousticness: Float,
        danceability : Float,
        energy : Float,
        instrumentalness : Float,
        speechiness : Float
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
               val userPreference = localDatabase.userPreferenceDao.get()
               val newPreference =  userPreference.copy(
                   acousticness = acousticness,
                   dancebility = danceability,
                   energy = energy,
                   instrumentalness = instrumentalness,
                   speechiness = speechiness
               )

                localDatabase.userPreferenceDao.insert(newPreference)
            }
        }
    }


    class FragmentAdjustParamsViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentAdjustParamsViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}