package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.playlist.asDomainModel
import com.ayushman.raindrop.domain.Playlist
import com.ayushman.raindrop.repository.PlaylistRepository
import com.ayushman.raindrop.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentUserProfileViewModel(context : Context) : ViewModel() {

    //Initialising the localDatabase and the Playlist repository
    private val localDatabase = LocalDatabase.getInstance(context)
    private val playlistRepository  = PlaylistRepository(localDatabase)
    private val userDataRepository = UserDataRepository(localDatabase)

    //Encapsulated LiveData object for observing the userData
    val userData = userDataRepository.userData

    private val _playlistData = MutableLiveData<List<Playlist>>()
    val playlistData : LiveData<List<Playlist>>
    get() = _playlistData


    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken()
                playlistRepository.refreshPlaylists(accessToken)
                _playlistData.postValue(localDatabase.playlistDao.get().asDomainModel())
            }
        }
    }

    //Clearing data function
    fun clearData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //Clearing the LocalDB for user
                localDatabase.userDataDao.clear()
                localDatabase.userPreferenceDao.clear()
                localDatabase.trackDao.clear()
                localDatabase.genreDao.clear()
                localDatabase.playlistDao.clear()
                localDatabase.weatherDataDao.clear()
                localDatabase.recommendationDao.clear()
                localDatabase.artistDao.clear()
                localDatabase.authTokenDao.clearData()
            }
        }
    }

    class FragmentUserProfileViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentUserProfileViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}