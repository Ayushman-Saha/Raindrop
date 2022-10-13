package com.ayushman.raindrop.repository

import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.network.SpotifyApi
import com.ayushman.raindrop.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenreDataRepository(private val localDatabase: LocalDatabase) {

    suspend fun refreshGenreData(accessToken : String) {

        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )

        withContext(Dispatchers.IO) {
            val artists = SpotifyApi.spotifyService.getGenresAsync(header).await()
            localDatabase.genreDao.clear()
            localDatabase.genreDao.insertAll(*artists.asDatabaseModel())
        }
    }

}