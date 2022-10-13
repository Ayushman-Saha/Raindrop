package com.ayushman.raindrop.database.authToken

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (authToken: AuthToken)

    @Query("SELECT * from auth_token WHERE id = 69")
    suspend fun get(): AuthToken?

    @Query("DELETE FROM auth_token")
    fun clearData()
}