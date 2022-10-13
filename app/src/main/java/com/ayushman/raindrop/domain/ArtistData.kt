package com.ayushman.raindrop.domain

data class ArtistData (
    val artistId  : String,
    val artistName : String,
    val artistImage : String,
    val artistUri : String,
    var isSelected : Boolean
)