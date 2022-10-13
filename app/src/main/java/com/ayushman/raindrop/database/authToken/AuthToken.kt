package com.ayushman.raindrop.database.authToken

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_token")
data class AuthToken(

    @PrimaryKey(autoGenerate = false)
    var id : Int = 69,

    @ColumnInfo(name = "user_id")
    var access_token :String,

    @ColumnInfo(name = "time")
    var time : Long = System.currentTimeMillis(),
)
