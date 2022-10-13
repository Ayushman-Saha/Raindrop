package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.trackData.asDomainModel
import com.ayushman.raindrop.domain.TrackData
import com.ayushman.raindrop.repository.TrackDataRepository
import com.ayushman.raindrop.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentSelectTracksViewModel(context: Context) : ViewModel() {

    //Getting the instance of the localDatabase
    private val localDatabase = LocalDatabase.getInstance(context)

    //Initialising the repositories
    private val trackDataRepository = TrackDataRepository(localDatabase)
    private val userDataRepository = UserDataRepository(localDatabase)


    //The liveData for showing the track data
    private val _trackData = MutableLiveData<List<TrackData>>()
    val trackData : LiveData<List<TrackData>>
        get() = _trackData

    //The liveData for showing the selected track data
    val selectedTrackData = trackDataRepository.selectedTrack

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                localDatabase.trackDao.clear()
            }
        }
    }

    fun queryTracks(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken()
                trackDataRepository.refreshGenreData(accessToken,query)
                val newQuery = query.replace(' ','%')
                _trackData.postValue(localDatabase.trackDao.getTrackByhref(newQuery).asDomainModel())
            }
        }
    }

    //Function to update the selected tracks to the database
    fun updateToPreference(track1 : TrackData, track2 : TrackData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pref = localDatabase.userPreferenceDao.get()
                val preference = pref.copy(
                    track1 = track1.trackId,
                    track2 = track2.trackId
                )
                localDatabase.userPreferenceDao.insert(preference)
            }
        }
    }

    fun refreshTracksOnQuery (query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _trackData.postValue(localDatabase.trackDao.getTrackByQuery(query).asDomainModel())
            }
        }
    }

    //Function to update selection in the database (Source of Truth)
    fun modifySelectedList(track : TrackData, boolean: Boolean, query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var item = localDatabase.trackDao.getTrackById(track.trackId)
                item = item.copy(
                    isSelected = boolean
                )
                localDatabase.trackDao.updateTrackData(item)
                _trackData.postValue(localDatabase.trackDao.getTrackByQuery(query).asDomainModel())
            }
        }
    }


    class FragmentTrackViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentSelectTracksViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}


