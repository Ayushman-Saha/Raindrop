package com.ayushman.raindrop.network

import com.ayushman.raindrop.database.artistData.DatabaseArtistData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkArtistDataContainer(

    @Json(name = "items")
    val networkArtistData : List<NetworkArtistData>

)

@JsonClass(generateAdapter = true)
data class NetworkArtistData(

    @Json(name = "id")
    val artistId : String,

    @Json(name = "name")
    val artistName : String,

    @Json(name = "uri")
    val artistUri : String,

    @Json(name = "images")
    val artistImage : List<ArtistImage>
)

@JsonClass(generateAdapter = true)
data class ArtistImage(
    val url : String
)

fun NetworkArtistDataContainer.asDatabaseModel() : Array<DatabaseArtistData> {
    return networkArtistData.map {
        DatabaseArtistData (
            artistId = it.artistId,
            artistName = it.artistName,
            artistImage = it.artistImage[0].url,
            artistUri = it.artistUri,
            isSelected = false
        )
    }.toTypedArray()
}