package com.ayushman.raindrop.database.genreData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayushman.raindrop.domain.GenreData

@Entity(tableName = "genre")
data class DatabaseGenreData(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "genre")
    val genre : String,

)

fun List<DatabaseGenreData>.asDomainModel() : List<GenreData> {
    return map {
        GenreData(
            genre = it.genre
        )
    }
}