package com.ayushman.raindrop.database.recommendationData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayushman.raindrop.domain.RecommendationTrackData

@Entity(tableName = "recommendation_data")
data class DatabaseRecommendationData(

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

    @ColumnInfo(name = "preview_url")
    val previewUrl : String
)

fun List<DatabaseRecommendationData>.asDomainModel() : List<RecommendationTrackData> {
    return map {
        RecommendationTrackData(
            trackId = it.trackId,
            trackUid = it.trackUid,
            trackName = it.trackName,
            trackCover = it.trackCover,
            trackArtist = it.trackArtist,
            previewUrl = it.previewUrl
        )
    }
}