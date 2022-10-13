package com.ayushman.raindrop.database.trackData

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (vararg trackData: DatabaseTrackData)

    @Query("SELECT * FROM track_data WHERE track_id = :trackId")
    fun getTrackById(trackId : String) : DatabaseTrackData

    @Query("SELECT * FROM track_data WHERE href LIKE '%' || :href || '%'")
    fun getTrackByhref( href : String) : List<DatabaseTrackData>

    @Query("SELECT * FROM track_data WHERE track_name LIKE :query || '%'")
    fun getTrackByQuery(query : String) : List<DatabaseTrackData>

    @Query("SELECT * FROM track_data WHERE is_selected = :value")
    fun getSelectedTracks(value : Boolean = true) : LiveData<List<DatabaseTrackData>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTrackData(track : DatabaseTrackData) : Void

    @Query(value = "DELETE FROM track_data")
    fun clear()
}