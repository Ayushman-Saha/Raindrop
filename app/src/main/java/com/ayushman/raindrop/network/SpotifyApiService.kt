package com.ayushman.raindrop.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


//The base url for spotify API
private const val BASE_URL = "https://api.spotify.com/v1/"

//The moshi adapter for parsing JSON
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//The interface for all Spotify API calls
interface SpotifyApiService {
    @GET("me")
    fun getUserDataAsync(@HeaderMap headers : Map<String,String>) : Deferred<NetworkUserData>

    @GET("me/playlists?limit = 50")
    fun getUserPlaylistsAsync(@HeaderMap headers: Map<String, String>) : Deferred<NetworkPlaylistDataContainer>

    @GET("me/top/artists?limit=100")
    fun getUserTopArtistsAsync(@HeaderMap headers: Map<String, String>) : Deferred<NetworkArtistDataContainer>

    @GET("recommendations/available-genre-seeds")
    fun getGenresAsync(@HeaderMap headers: Map<String, String>) : Deferred<NetworkGenreData>

    @GET("search")
    fun getTrackDataOnQueryAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("q") query : String,
        @Query("type") type : String = "track",
        @Query("limit") limit: Int = 10
    ) : Deferred<NetworkTrackDataContainer>

    @GET("recommendations")
    fun getRecommendationResultAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("limit") limit: Int = 100,
        @QueryMap(encoded = true) query : Map<String,String>
    ) : Deferred<NetworkRecommendationDataContainer>

    @POST("playlists/{playlist_id}/tracks")
    fun addTrackToPlaylistAsync(
        @HeaderMap headers : Map<String,String>,
        @Path("playlist_id") playlistId : String,
        @Query("uris") trackUri : String
    ) : Deferred<NetworkSnapshotDataObjects>

    @POST("users/{user_id}/playlists")
    fun addNewPlaylistAsync(
        @HeaderMap headers: Map<String, String>,
        @Path("user_id") userId : String,
        @Body() body : Map<String,@JvmSuppressWildcards Any>
    ) : Deferred<NetworkSnapshotDataObjects>
}

//Exposing the services using object
object SpotifyApi {
    //The retrofit object
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val spotifyService: SpotifyApiService =
        retrofit.create(SpotifyApiService::class.java)
}
