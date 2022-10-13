package com.ayushman.raindrop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.artistData.asDomainModel
import com.ayushman.raindrop.domain.ArtistData
import com.ayushman.raindrop.network.SpotifyApi
import com.ayushman.raindrop.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistDataRepository(private val localDatabase: LocalDatabase) {

    val selectedArtist : LiveData<List<ArtistData>> = Transformations.map(localDatabase.artistDao.getSelectedArtists()) {
        it?.asDomainModel()
    }

    suspend fun refreshArtistData(accessToken : String) {

        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )

        withContext(Dispatchers.IO) {
            val artists = SpotifyApi.spotifyService.getUserTopArtistsAsync(header).await()
            localDatabase.artistDao.clear()
            localDatabase.artistDao.insertAll(*artists.asDatabaseModel())
        }
    }
}