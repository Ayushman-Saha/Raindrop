package com.ayushman.raindrop

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ayushman.raindrop.ui.fragments.*
import com.ayushman.raindrop.ui.theme.RaindropTheme

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            RaindropTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "fragmentWeatherHome") {
                        composable("fragmentWeatherHome") { FragmentWeatherHome(this@MainActivity, navController) }
                        composable("fragmentSelectArtists") { FragmentSelectArtists(this@MainActivity,navController)}
                        composable("fragmentSelectGenre") { FragmentSelectGenre(this@MainActivity,navController)}
                        composable("fragmentSelectTrack") { FragmentSelectTrack(this@MainActivity,navController)}
                        composable("fragmentAdjustParams"){ FragmentAdjustParams(this@MainActivity,navController)}
                        composable("fragmentConfirmParameters") { FragmentConfirmParameters(this@MainActivity,navController)}
                        composable("fragmentRecommendationResult") {mediaPlayer = fragmentRecommendationResult(this@MainActivity,navController) }
                        composable("fragmentUserProfile") { FragmentUserProfile(this@MainActivity)}
                        composable(
                            "fragmentSelectPlaylist/{trackId}",
                            arguments = listOf(navArgument("trackId"){type = NavType.StringType})
                        ) {
                            FragmentSelectPlaylist(this@MainActivity,navController,it.arguments?.getString("trackId")!!)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
    }
}

