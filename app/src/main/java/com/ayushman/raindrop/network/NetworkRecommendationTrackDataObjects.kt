package com.ayushman.raindrop.network

import com.ayushman.raindrop.database.recommendationData.DatabaseRecommendationData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRecommendationDataContainer(
    val tracks : List<NetworkRecommendationTrack>
)

@JsonClass(generateAdapter = true)
data class NetworkRecommendationTrack (

    @Json(name = "id")
    val trackId : String,

    @Json(name = "uri")
    val trackUid : String,

    @Json(name = "album")
    val trackCover : AlbumDetails,

    @Json(name = "name")
    val trackName : String,

    @Json(name = "artists")
    val artists : List<ArtistDetails>,

    @Json(name = "preview_url")
    val previewUrl : String?
    )


fun NetworkRecommendationDataContainer.asDatabaseModel() : Array<DatabaseRecommendationData> {
    return tracks.map {
        if(it.previewUrl != null)
            DatabaseRecommendationData(
                trackId = it.trackId,
                trackArtist = it.artists[0].artistName,
                trackCover = it.trackCover.albumImage[0].url,
                trackName = it.trackName,
                trackUid = it.trackUid,
                previewUrl = it.previewUrl
            )
        else
            DatabaseRecommendationData(
                trackId = it.trackId,
                trackArtist = it.artists[0].artistName,
                trackCover = it.trackCover.albumImage[0].url,
                trackName = it.trackName,
                trackUid = it.trackUid,
                previewUrl = ""
            )
    }.toTypedArray()
}