package com.ayushman.raindrop.repository

import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.network.SpotifyApi
import com.ayushman.raindrop.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RecommendationRepository(private val localDatabase: LocalDatabase) {

    suspend fun refreshRecommendationData(accessToken: String, queryMap: Map<String, String>) {

        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )

        withContext(Dispatchers.IO) {
            val tracks = SpotifyApi.spotifyService.getRecommendationResultAsync(header, query = queryMap).await()
            localDatabase.recommendationDao.clear()
            localDatabase.recommendationDao.insertAll(*tracks.asDatabaseModel())
        }

    }

}