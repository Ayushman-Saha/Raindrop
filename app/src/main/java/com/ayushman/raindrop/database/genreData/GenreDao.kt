package com.ayushman.raindrop.database.genreData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (vararg artistData: DatabaseGenreData)

    @Query("SELECT * FROM genre")
    fun get() : List<DatabaseGenreData>

    @Query("SELECT * FROM genre WHERE genre = :artistId")
    fun getById(artistId: String) : DatabaseGenreData

    @Query("SELECT * FROM genre WHERE genre LIKE :query || '%'")
    fun getArtistBySearch(query : String) : List<DatabaseGenreData>

    @Query(value = "DELETE FROM genre")
    fun clear()
}