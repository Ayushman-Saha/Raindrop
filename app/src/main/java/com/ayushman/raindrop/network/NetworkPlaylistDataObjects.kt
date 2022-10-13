package com.ayushman.raindrop.network

import com.ayushman.raindrop.database.playlist.DatabasePlaylist
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Data class to hold all the playlists of user
@JsonClass(generateAdapter = true)
data class NetworkPlaylistDataContainer (

    @Json(name = "items")
    val networkPlaylistData: List<NetworkPlaylistData>
)


//Data class to hold a particular playlist
@JsonClass(generateAdapter = true)
data class NetworkPlaylistData (

    @Json(name = "id")
    val playlistId : String,

    @Json(name = "images")
    val playlistCover : List<ImageUrl>,

    @Json(name = "name")
    val playlistName : String,

    @Json(name =  "uri")
    val playlistUri : String,

    @Json(name = "tracks")
    val trackCount : TrackCount
)

//Data class to hold the track count
data class TrackCount(
    val total : Int
)

//Transformations to Database object
fun NetworkPlaylistDataContainer.asDataModel() : Array<DatabasePlaylist> {
    return networkPlaylistData.map {
        if(it.playlistCover.isNotEmpty()) {
            DatabasePlaylist(
                playlistId = it.playlistId,
                playlistName = it.playlistName,
                playlistUri = it.playlistUri,
                playlistCover = it.playlistCover[0].url,
                trackCount = "${it.trackCount.total} tracks"
            )
        } else {
            DatabasePlaylist(
                playlistId = it.playlistId,
                playlistName = it.playlistName,
                playlistUri = it.playlistUri,
                playlistCover = "https://source.unsplash.com/random",
                trackCount = "${it.trackCount.total} tracks"
            )
        }
    }.toTypedArray()
}