package com.ayushman.raindrop.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ayushman.raindrop.database.artistData.ArtistDao
import com.ayushman.raindrop.database.artistData.DatabaseArtistData
import com.ayushman.raindrop.database.authToken.AuthToken
import com.ayushman.raindrop.database.authToken.AuthTokenDao
import com.ayushman.raindrop.database.genreData.DatabaseGenreData
import com.ayushman.raindrop.database.genreData.GenreDao
import com.ayushman.raindrop.database.playlist.DatabasePlaylist
import com.ayushman.raindrop.database.playlist.PlaylistDao
import com.ayushman.raindrop.database.recommendationData.DatabaseRecommendationData
import com.ayushman.raindrop.database.recommendationData.RecommendationDao
import com.ayushman.raindrop.database.trackData.DatabaseTrackData
import com.ayushman.raindrop.database.trackData.TrackDao
import com.ayushman.raindrop.database.userData.DatabaseUserData
import com.ayushman.raindrop.database.userData.UserDataDao
import com.ayushman.raindrop.database.userPreference.DatabaseUserPreference
import com.ayushman.raindrop.database.userPreference.UserPreferenceDao
import com.ayushman.raindrop.database.weatherData.DatabaseWeatherData
import com.ayushman.raindrop.database.weatherData.WeatherDataDao

@Database(entities = [
    DatabaseUserData::class,
    AuthToken::class,
    DatabaseWeatherData::class,
    DatabaseArtistData::class,
    DatabaseUserPreference::class,
    DatabaseGenreData::class,
    DatabaseTrackData::class,
    DatabaseRecommendationData::class,
    DatabasePlaylist::class
                     ],
    version = 2,exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract val userDataDao : UserDataDao
    abstract val authTokenDao : AuthTokenDao
    abstract val weatherDataDao : WeatherDataDao
    abstract val artistDao : ArtistDao
    abstract val userPreferenceDao : UserPreferenceDao
    abstract val genreDao : GenreDao
    abstract val trackDao : TrackDao
    abstract val recommendationDao : RecommendationDao
    abstract val playlistDao : PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE : LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            synchronized(this) {
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "local_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }

}