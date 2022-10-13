package com.ayushman.raindrop.domain

data class TrackData(
    val trackId : String,
    val trackUid : String,
    val trackCover : String,
    val trackArtist : String,
    val trackName : String,
    val isSelected : Boolean,
    val href : String
)
