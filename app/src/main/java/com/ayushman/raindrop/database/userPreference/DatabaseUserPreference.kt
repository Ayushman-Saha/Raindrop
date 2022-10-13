package com.ayushman.raindrop.database.userPreference

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preference")
data class DatabaseUserPreference(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int = 69,

    @ColumnInfo(name = "artist_1")
    val artist1 : String,

    @ColumnInfo(name = "artist_2")
    val artist2 : String,

    @ColumnInfo(name = "genre")
    val genre : String,

    @ColumnInfo(name = "track_1")
    val track1 : String,

    @ColumnInfo(name = "track_2")
    val track2: String,

    @ColumnInfo(name = "acousticness")
    val acousticness : Float,

    @ColumnInfo(name = "dancebility")
    val dancebility : Float,

    @ColumnInfo(name = "energy")
    val energy : Float,

    @ColumnInfo(name = "instrumentalness")
    val instrumentalness : Float,

    @ColumnInfo(name = "speechiness")
    val speechiness : Float
)
