package com.ayushman.raindrop.database.userPreference

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserPreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (preference : DatabaseUserPreference)

    @Query("SELECT * FROM user_preference")
    fun get() : DatabaseUserPreference

    @Query(value = "DELETE FROM user_preference")
    fun clear()
}