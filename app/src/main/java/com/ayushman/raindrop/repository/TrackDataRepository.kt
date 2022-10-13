package com.ayushman.raindrop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.trackData.asDomainModel
import com.ayushman.raindrop.domain.TrackData
import com.ayushman.raindrop.network.SpotifyApi
import com.ayushman.raindrop.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackDataRepository (private val localDatabase: LocalDatabase) {

    val selectedTrack : LiveData<List<TrackData>> = Transformations.map(localDatabase.trackDao.getSelectedTracks()) {
        it?.asDomainModel()
    }

    suspend fun refreshGenreData(accessToken : String, query : String) {

        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )

        withContext(Dispatchers.IO) {
            val tracks = SpotifyApi.spotifyService.getTrackDataOnQueryAsync(header,query).await()
            localDatabase.trackDao.insertAll(*tracks.asDatabaseModel())
        }
    }
}