package com.ayushman.raindrop.domain

data class UserData (
    val userId : String,
    val email : String,
    val displayName : String,
    val displayPicture : String,
    val followers : Int,
    val city : String
)
