package com.ayushman.raindrop.database.playlist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (vararg userData: DatabasePlaylist)

    @Query("SELECT * FROM playlists")
    fun get() : List<DatabasePlaylist>

    @Query("SELECT * FROM playlists WHERE playlist_name LIKE :query || '%'")
    fun getByQuery(query : String) : List<DatabasePlaylist>

    @Query("SELECT * FROM playlists WHERE playlist_id = :playlistId")
    fun getPlaylistDataById (playlistId : String) : LiveData<List<DatabasePlaylist>>

    @Query(value = "DELETE FROM playlists")
    fun clear()
}