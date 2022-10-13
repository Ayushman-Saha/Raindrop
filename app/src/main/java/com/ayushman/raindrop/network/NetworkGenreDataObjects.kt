package com.ayushman.raindrop.network

import com.ayushman.raindrop.database.genreData.DatabaseGenreData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkGenreData (
    val genres : List<String>
    )

fun NetworkGenreData.asDatabaseModel() : Array<DatabaseGenreData> {
    return genres.map {
        DatabaseGenreData (
            genre = it
        )
    }.toTypedArray()
}