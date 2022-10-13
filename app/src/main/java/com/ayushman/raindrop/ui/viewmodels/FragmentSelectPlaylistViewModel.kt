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

class FragmentSelectPlaylistViewModel(context : Context) : ViewModel() {

    //Getting the instance of the localDatabase
    private val localDatabase = LocalDatabase.getInstance(context)

    //Initialising the repositories
    private val playlistDataRepository = PlaylistRepository(localDatabase)
    private val userDataRepository = UserDataRepository(localDatabase)


    //The liveData for showing the artist data
    private val _playlistData = MutableLiveData<List<Playlist>>()
    val playlistData : LiveData<List<Playlist>>
        get() = _playlistData

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken()
                playlistDataRepository.refreshPlaylists(accessToken)
                _playlistData.postValue(localDatabase.playlistDao.get().asDomainModel())
            }
        }
    }

    fun refreshPlaylistDataOnQuery(query : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val playlists = localDatabase.playlistDao.getByQuery(query).asDomainModel()
                _playlistData.postValue(playlists)
            }
        }
    }

    fun addTrackToPlaylist(playlistId :String, trackUri : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken()
                playlistDataRepository.addTrackToPlaylist(accessToken,trackUri,playlistId)
            }
        }
    }

    fun addNewPlaylist(playlistName : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken()
                val userId = localDatabase.userDataDao.get()?.userId!!

                playlistDataRepository.addNewPlaylist(accessToken,playlistName,userId)

                playlistDataRepository.refreshPlaylists(accessToken)
                val updatedPlaylists =localDatabase.playlistDao.get().asDomainModel()
                _playlistData.postValue(updatedPlaylists)
            }
        }
    }

    class FragmentSelectPlaylistViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentSelectPlaylistViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}