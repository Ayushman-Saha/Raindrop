package com.ayushman.raindrop.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkSnapshotDataObjects(
    @Json(name = "snapshot_id")
    val snapShotId : String
)