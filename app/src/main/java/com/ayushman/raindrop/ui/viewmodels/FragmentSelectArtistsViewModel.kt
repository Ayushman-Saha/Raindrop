package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.artistData.asDomainModel
import com.ayushman.raindrop.database.userPreference.DatabaseUserPreference
import com.ayushman.raindrop.domain.ArtistData
import com.ayushman.raindrop.repository.ArtistDataRepository
import com.ayushman.raindrop.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentSelectArtistsViewModel(context : Context) : ViewModel() {

    //Getting the instance of the localDatabase
    private val localDatabase = LocalDatabase.getInstance(context)

    //Initialising the repositories
    private val artistDataRepository = ArtistDataRepository(localDatabase)
    private val userDataRepository = UserDataRepository(localDatabase)


    //The liveData for showing the artist data
    private val _artistData = MutableLiveData<List<ArtistData>>()
    val artistData : LiveData<List<ArtistData>>
        get() = _artistData

    //The liveData for showing the selected artists
    private val _selectedArtists = MutableLiveData<MutableSet<ArtistData>>()
    val selectedArtists : LiveData<MutableSet<ArtistData>>
        get() = _selectedArtists

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken() //Getting accessToken
                artistDataRepository.refreshArtistData(accessToken) //Refresh artist data before showing
                _artistData.postValue(localDatabase.artistDao.get().asDomainModel()) //Setting the value for livedata
                _selectedArtists.postValue(mutableSetOf())  //Empty the set for selecting artist
            }
        }
    }

    //Function to refreshData of artist based on query params
    fun refreshArtistDataOnQuery(query : String) {
        _artistData.value = mutableListOf()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _artistData.postValue(localDatabase.artistDao.getArtistBySearch(query).asDomainModel())
            }
        }
    }

    //Function to update selection in the database (Source of Truth)
    fun modifySelectedList(artist : ArtistData, boolean: Boolean, query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var item = localDatabase.artistDao.getById(artist.artistId)
                item = item.copy(
                    isSelected = boolean
                )
                localDatabase.artistDao.updateArtistData(item)
                _artistData.postValue(localDatabase.artistDao.getArtistBySearch(query).asDomainModel())
            }
        }

        if (boolean)
            _selectedArtists.value?.add(artist)
        else
            _selectedArtists.value?.remove(artist.copy(isSelected = false))
    }

    //Function to update the selected artists to the database
    fun updateToPreference() {
        val artist1 = _selectedArtists.value?.elementAt(0)
        val artist2 = _selectedArtists.value?.elementAt(1)

        val preference = DatabaseUserPreference(
            artist1 = artist1!!.artistId,
            artist2 = artist2!!.artistId,
            genre = "", track1 = "", track2 = "", acousticness = 0.0F, instrumentalness = 0.0F, dancebility = 0.0F, energy = 0.0F, speechiness = 0.0F
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                localDatabase.userPreferenceDao.insert(preference)
            }
        }
    }

    class FragmentArtistsViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentSelectArtistsViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}