package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.domain.GenreData
import com.ayushman.raindrop.repository.ArtistDataRepository
import com.ayushman.raindrop.repository.TrackDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentConfirmParamsViewModel(context: Context) : ViewModel() {

    //Getting the instance of the localDatabase
    private val localDatabase = LocalDatabase.getInstance(context)

    //Initialising the repositories
    private val artistDataRepository = ArtistDataRepository(localDatabase)
    private val trackDataRepository = TrackDataRepository(localDatabase)

    //The observables
    val selectedArtists = artistDataRepository.selectedArtist
    val selectedTracks = trackDataRepository.selectedTrack

    private val _selectedGenre = MutableLiveData<GenreData>()
    val selectedGenre : LiveData<GenreData>
    get() = _selectedGenre

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userPreference = localDatabase.userPreferenceDao.get()
                val genreData = GenreData(genre = userPreference.genre)
                _selectedGenre.postValue(genreData)
            }
        }
    }

    class FragmentConfirmParamsViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentConfirmParamsViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}