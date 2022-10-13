package com.ayushman.raindrop.database.artistData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayushman.raindrop.domain.ArtistData

@Entity(tableName = "artist_data")
data class DatabaseArtistData(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "artist_id")
    val artistId : String,

    @ColumnInfo(name = "artist_image")
    val artistImage : String,

    @ColumnInfo(name = "artist_name")
    val artistName : String,

    @ColumnInfo(name = "artist_uri")
    val artistUri : String,

    @ColumnInfo(name = "is_selected")
    val isSelected : Boolean

)

fun List<DatabaseArtistData>.asDomainModel() : List<ArtistData> {
    return map {
        ArtistData(
            artistId = it.artistId,
            artistName = it.artistName,
            artistImage = it.artistImage,
            artistUri = it.artistUri,
            isSelected = it.isSelected
        )
    }
}