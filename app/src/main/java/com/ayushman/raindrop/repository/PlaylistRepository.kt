package com.ayushman.raindrop.repository

import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.network.SpotifyApi
import com.ayushman.raindrop.network.asDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepository (private val localDatabase: LocalDatabase) {

    //Function to refresh the Playlists from Spotify API
    suspend fun refreshPlaylists(accessToken : String) {
        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )
        withContext(Dispatchers.IO) {
            val playlists = SpotifyApi.spotifyService.getUserPlaylistsAsync(header).await()
            localDatabase.playlistDao.clear() //Clearing the old playlists
            localDatabase.playlistDao.insertAll(*playlists.asDataModel()) //Adding the new playlists
        }
    }

    suspend fun addTrackToPlaylist(accessToken: String, trackUri : String, playlistId : String) {
        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )
        withContext(Dispatchers.IO) {
           SpotifyApi.spotifyService.addTrackToPlaylistAsync(header,playlistId,trackUri).await()
        }
    }

    suspend fun addNewPlaylist(accessToken: String, playlistName : String, userId : String) {
        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )

        val playlistNameObject = mapOf(
            "name" to playlistName,
            "description" to "Created on Raindrop App",
            "public" to false
        )

        withContext(Dispatchers.IO) {
            SpotifyApi.spotifyService.addNewPlaylistAsync(header, userId,playlistNameObject).await()
        }
    }
}