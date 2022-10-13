package com.ayushman.raindrop.network

import com.ayushman.raindrop.database.userData.DatabaseUserData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Network object to store the userData
@JsonClass(generateAdapter = true)
data class NetworkUserData(

    @Json(name = "id")
    val userId  : String,
    val email  : String,


    @Json(name = "display_name")
    val displayName : String,

    @Json(name = "images")
    val displayPicture : List<ImageUrl>,
    val followers : FollowerNumber
)

//Object to parse the url array
data class ImageUrl(
    val url : String
)

//Object to parse the follower JSON object
data class FollowerNumber(
    val total : Int
)

//Transformation to Database object
fun NetworkUserData.asDatabaseModel (cityName : String) : DatabaseUserData {
    if(displayPicture.isNotEmpty()) {
        return DatabaseUserData(
            userId = userId,
            email = email,
            displayName = displayName,
            displayPicture = displayPicture[0].url,
            followers = followers.total,
            isError = false,
            city = cityName
        )
    }
    else {
        return DatabaseUserData(
            userId = userId,
            email = email,
            displayName = displayName,
            displayPicture = "https://source.unsplash.com/random",
            followers = followers.total,
            isError = false,
            city = cityName
        )
    }
}
