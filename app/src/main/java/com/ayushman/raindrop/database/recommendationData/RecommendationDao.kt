package com.ayushman.raindrop.database.recommendationData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecommendationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg trackData: DatabaseRecommendationData)

    @Query("SELECT * FROM recommendation_data WHERE track_name LIKE :query || '%'")
    fun getTrackByQuery(query : String) : List<DatabaseRecommendationData>

    @Query(value = "DELETE FROM recommendation_data")
    fun clear()
}