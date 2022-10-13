package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.genreData.asDomainModel
import com.ayushman.raindrop.domain.GenreData
import com.ayushman.raindrop.repository.GenreDataRepository
import com.ayushman.raindrop.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentSelectGenreViewModel(context: Context) : ViewModel() {

    //Getting the instance of the localDatabase
    private val localDatabase = LocalDatabase.getInstance(context)

    //Initialising the repositories
    private val genreDataRepository = GenreDataRepository(localDatabase)
    private val userDataRepository = UserDataRepository(localDatabase)


    //The liveData for showing the genre data
    private val _genreData = MutableLiveData<List<GenreData>>()
    val genreData : LiveData<List<GenreData>>
        get() = _genreData

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken() //Getting accessToken
                genreDataRepository.refreshGenreData(accessToken) //Refresh genre data before showing
                _genreData.postValue(localDatabase.genreDao.get().asDomainModel()) //Setting the value for livedata
            }
        }
    }

    //Function to refreshData of genre based on query params
    fun refreshArtistDataOnQuery(query : String) {
        _genreData.value = mutableListOf()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _genreData.postValue(localDatabase.genreDao.getArtistBySearch(query).asDomainModel())
            }
        }
    }

    //Function to update the selected genre to the database
    fun updateToPreference(genre : GenreData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pref = localDatabase.userPreferenceDao.get()
                val preference = pref.copy(
                    genre = genre.genre
                )
                localDatabase.userPreferenceDao.insert(preference)
            }
        }
    }


    class FragmentGenreViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentSelectGenreViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}