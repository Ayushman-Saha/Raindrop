package com.ayushman.raindrop.database.userData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (databaseUserData: DatabaseUserData)

    @Query("SELECT * FROM user_data WHERE id = 69")
    suspend fun get(): DatabaseUserData?

    @Query("SELECT * FROM user_data WHERE id = 69")
    fun getUserDataObservable(): LiveData<List<DatabaseUserData>>

    @Query(value = "DELETE FROM user_data")
    fun clear()

}