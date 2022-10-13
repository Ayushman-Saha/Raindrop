package com.ayushman.raindrop.database.artistData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (vararg artistData: DatabaseArtistData)

    @Query("SELECT * FROM artist_data")
    fun get() : List<DatabaseArtistData>

    @Query("SELECT * FROM artist_data WHERE artist_id = :artistId")
    fun getById(artistId: String) : DatabaseArtistData

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateArtistData (artistData : DatabaseArtistData) : Int

    @Query("SELECT * FROM artist_data WHERE artist_name LIKE :query || '%'")
    fun getArtistBySearch(query : String) : List<DatabaseArtistData>

    @Query("SELECT * FROM artist_data WHERE is_selected = :isSelected")
    fun getSelectedArtists(isSelected : Boolean = true) : LiveData<List<DatabaseArtistData>>

    @Query(value = "DELETE FROM artist_data")
    fun clear()

}