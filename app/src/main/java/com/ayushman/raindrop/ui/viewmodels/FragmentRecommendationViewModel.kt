package com.ayushman.raindrop.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.recommendationData.asDomainModel
import com.ayushman.raindrop.domain.RecommendationTrackData
import com.ayushman.raindrop.repository.RecommendationRepository
import com.ayushman.raindrop.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentRecommendationViewModel(context: Context) : ViewModel() {

    //Getting the instance of the localDatabase
    private val localDatabase = LocalDatabase.getInstance(context)

    //Initialising the repositories
    private val recommendationDataRepository = RecommendationRepository(localDatabase)
    private val userDataRepository = UserDataRepository(localDatabase)

    //The liveData for showing the recommendation data
    private val _recommendationData = MutableLiveData<List<RecommendationTrackData>>()
    val recommendationData : LiveData<List<RecommendationTrackData>>
        get() = _recommendationData

    //The liveData for showing the selected track for preview
    private val _previewTrack = MutableLiveData<RecommendationTrackData>()
    val previewTrack : LiveData<RecommendationTrackData>
        get() = _previewTrack

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = userDataRepository.getToken()
                val preferences = localDatabase.userPreferenceDao.get()

                val queryMap = mutableMapOf(
                    "seed_artists" to "${preferences.artist1},${preferences.artist2}",
                    "seed_genres" to preferences.genre,
                    "seed_tracks" to "${preferences.track1},${preferences.track2}",
                    "target_acousticness" to preferences.acousticness.toString(),
                    "target_danceability" to preferences.dancebility.toString(),
                    "target_energy" to preferences.energy.toString(),
                    "target_instrumentalness" to preferences.instrumentalness.toString(),
                    "target_speechiness" to preferences.speechiness.toString()
                    )

                queryMap.values.remove("0.0f")
                recommendationDataRepository.refreshRecommendationData(accessToken,queryMap)
                _recommendationData.postValue(localDatabase.recommendationDao.getTrackByQuery("").asDomainModel())
            }
        }
    }

    fun refreshRecommendationListOnQuery(query : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val results = localDatabase.recommendationDao.getTrackByQuery(query)
                _recommendationData.postValue(results.asDomainModel())
            }
        }
    }

    fun changePreviewTrack(newTrack: RecommendationTrackData?) {
        _previewTrack.value = newTrack
    }

    class FragmentRecommendationViewModelFactory(private val context: Context) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = FragmentRecommendationViewModel(context) as T
    }

    //To prevent memory leaks
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}