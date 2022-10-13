package com.ayushman.raindrop.database.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayushman.raindrop.domain.Playlist

@Entity(tableName = "playlists")
data class DatabasePlaylist(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "playlist_id")
    val playlistId : String,

    @ColumnInfo(name = "playlist_cover")
    val playlistCover : String,

    @ColumnInfo(name = "playlist_name")
    val playlistName : String,

    @ColumnInfo(name = "playlist_uri")
    val playlistUri : String,

    @ColumnInfo(name = "track_count")
    val trackCount : String
)


fun List<DatabasePlaylist>.asDomainModel() : List<Playlist> {
    return map {
        Playlist(
            playlistId =  it.playlistId,
            playlistCover = it.playlistCover,
            playlistName = it.playlistName,
            playlistUri = it.playlistUri,
            trackCount = it.trackCount
        )
    }
}

