package com.ayushman.raindrop.ui.fragments

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ayushman.raindrop.ui.composables.ArtistGridView
import com.ayushman.raindrop.ui.composables.GenreGridView
import com.ayushman.raindrop.ui.composables.TrackListItems
import com.ayushman.raindrop.ui.viewmodels.FragmentConfirmParamsViewModel

@ExperimentalFoundationApi
@Composable
fun FragmentConfirmParameters(context: Context, navController: NavController) {

    //Initialising the viewModel
    val viewModel : FragmentConfirmParamsViewModel = viewModel(
        factory = FragmentConfirmParamsViewModel.FragmentConfirmParamsViewModelFactory(context)
    )

    //The state holders
    val selectedArtistsState = viewModel.selectedArtists.observeAsState()
    val selectedTracksState = viewModel.selectedTracks.observeAsState()
    val selectedGenreState = viewModel.selectedGenre.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(
                state = rememberScrollState()
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //The header
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            //Text for showing the header
            Text(
                modifier = Modifier.padding(top = 32.dp, start = 4.dp),
                text = "Confirm parameters",
                style = MaterialTheme.typography.h1
            )

            //Text for showing the instructions
            Text(
                modifier = Modifier.padding(start = 4.dp, bottom = 16.dp),
                text = "Review the selected parameters to continue",
                style = MaterialTheme.typography.body1
            )
        }

        //The selected artist column
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Selected artists:",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Start
            )

            //The column for the selection for the artists
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedArtistsState.value?.let { mainList ->
                        //State for the individual items
                        val checkedState = mutableStateOf(true)

                    Box(
                        modifier = Modifier.padding(4.dp),
                    ) {
                        ArtistGridView(
                            artistData = mainList[0],
                            checkedState = checkedState.value,
                            onClick = {}
                        )
                    }

                    Box(
                        modifier = Modifier.padding(4.dp),
                    ) {
                        ArtistGridView(
                            artistData = mainList[1],
                            checkedState = checkedState.value,
                            onClick = {}
                        )
                    }
                }
            }
        }

        //The column for showing genre
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Selected genre:",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Start
            )

            selectedGenreState.value?.let {
                GenreGridView(
                    genreData = it,
                    onClick = {}
                )
            }
        }

        //The column for showing selected tracks
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Selected tracks:",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Start
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                selectedTracksState.value?.let {
                    it.forEach {item ->

                        //State for the individual items
                        val checkedState = mutableStateOf(item.isSelected)

                        TrackListItems(
                            trackData = item,
                            checkedState = checkedState.value,
                            onClick = {}
                        )
                    }
                }
            }
        }

        //The button for building
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment =  Alignment.BottomCenter
        ) {
            //Button to proceed to build the playlist
            Button(
                onClick = {
                    navController.navigate("fragmentRecommendationResult") {
                        popUpTo("fragmentWeatherHome")
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(15)
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

}

