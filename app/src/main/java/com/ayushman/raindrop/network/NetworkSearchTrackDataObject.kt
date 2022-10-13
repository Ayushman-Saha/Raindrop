package com.ayushman.raindrop.network

import com.ayushman.raindrop.database.trackData.DatabaseTrackData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkTrackDataContainer(
    val tracks : NetworkTracks
)

@JsonClass(generateAdapter = true)
data class NetworkTracks (
    val items : List<NetworkTrackData>,
    val href : String
)

@JsonClass(generateAdapter = true)
data class NetworkTrackData(

    @Json(name = "id")
    val trackId : String,

    @Json(name = "uri")
    val trackUid : String,

    @Json(name = "album")
    val trackCover : AlbumDetails,

    @Json(name = "name")
    val trackName : String,

    @Json(name = "artists")
    val artists : List<ArtistDetails>
)

//Data class to hold artist details
@JsonClass(generateAdapter = true)
data class ArtistDetails (
    @Json(name = "name")
    val artistName : String
)

//Data class to hold album details
data class AlbumDetails (
    @Json(name = "images")
    val albumImage : List<ImageUrl>
)

//Transformation to Database objects
fun NetworkTrackDataContainer.asDatabaseModel() : Array<DatabaseTrackData> {
    return tracks.items.map {
        DatabaseTrackData(
            trackId = it.trackId,
            trackUid = it.trackUid,
            trackArtist = it.artists[0].artistName,
            trackCover = it.trackCover.albumImage[0].url,
            trackName = it.trackName,
            isSelected = false,
            href = tracks.href
        )
    }.toTypedArray()
}
