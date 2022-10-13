package com.ayushman.raindrop.database.trackData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayushman.raindrop.domain.TrackData

@Entity(tableName = "track_data")
data class DatabaseTrackData(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "track_id")
    val trackId : String,

    @ColumnInfo(name = "track_uid")
    val trackUid : String,

    @ColumnInfo(name = "track_cover")
    val trackCover : String,

    @ColumnInfo(name = "artist")
    val trackArtist : String,

    @ColumnInfo(name = "track_name")
    val trackName : String,

    @ColumnInfo(name = "is_selected")
    val isSelected : Boolean,

    @ColumnInfo(name = "href")
    val href : String

)

fun List<DatabaseTrackData>.asDomainModel() : List<TrackData> {
    return map {
        TrackData(
            trackId = it.trackId,
            trackUid = it.trackUid,
            trackCover = it.trackCover,
            trackArtist = it.trackArtist,
            trackName = it.trackName,
            isSelected = it.isSelected,
            href = it.href
        )
    }
}