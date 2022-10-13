package com.ayushman.raindrop.database.userData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayushman.raindrop.domain.UserData

@Entity(tableName = "user_data")
data class DatabaseUserData(

    @PrimaryKey(autoGenerate = false)
    var id : Int = 69,

    @ColumnInfo(name = "user_id")
    var userId :String,

    @ColumnInfo(name = "email")
    var email : String,

    @ColumnInfo(name = "display_name")
    var displayName  : String,

    @ColumnInfo(name =  "display_picture")
    var displayPicture : String,

    @ColumnInfo(name = "followers")
    var followers : Int,

    @ColumnInfo(name= "is_error")
    var isError : Boolean,

    @ColumnInfo(name = "city")
    var city : String
)

fun List<DatabaseUserData>.asDomainModel() : List<UserData> {
    return map {
        UserData(
            userId = it.userId,
            email = it.email,
            displayPicture = it.displayPicture,
            displayName = it.displayName,
            followers = it.followers,
            city = it.city
        )
    }
}