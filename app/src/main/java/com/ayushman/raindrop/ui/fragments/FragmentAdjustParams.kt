package com.ayushman.raindrop.ui.fragments

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ayushman.raindrop.ui.theme.BackgroundWhite
import com.ayushman.raindrop.ui.theme.PrimaryBlack
import com.ayushman.raindrop.ui.viewmodels.FragmentAdjustParamsViewModel

@Composable
fun FragmentAdjustParams(context: Context, navController: NavHostController) {

    var acousticness by remember { mutableStateOf(0.0F)}
    var dancebility by remember { mutableStateOf(0.0F)}
    var energy by remember { mutableStateOf(0.0F)}
    var instrumentalness by remember { mutableStateOf(0.0F)}
    var speechiness by remember { mutableStateOf(0.0F)}

    //Initialising the viewModel
    val viewModel : FragmentAdjustParamsViewModel = viewModel(
        factory = FragmentAdjustParamsViewModel.FragmentAdjustParamsViewModelFactory(context)
    )

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    text = "Adjust parameters",
                    style = MaterialTheme.typography.h1
                )

                //Text for showing the instructions
                Text(
                    modifier = Modifier.padding(start = 4.dp, bottom = 16.dp),
                    text = "Set the parameter to 0 to ignore it",
                    style = MaterialTheme.typography.body1
                )
            }

            //Column for acousticness
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Acousticness: ${String.format("%.2f",acousticness)}",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start
                )

                Slider(
                    modifier = Modifier.padding(bottom = 4.dp),
                    value = acousticness,
                    onValueChange = {
                        acousticness = it
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = BackgroundWhite,
                        activeTrackColor = PrimaryBlack
                    )
                )
            }

            //Column for danceability
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Danceability: ${String.format("%.2f",dancebility)}",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start
                )

                Slider(
                    modifier = Modifier.padding(bottom = 4.dp),
                    value = dancebility,
                    onValueChange = {
                        dancebility = it
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = BackgroundWhite,
                        activeTrackColor = PrimaryBlack
                    )
                )
            }

            //Column for energy
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Energy: ${String.format("%.2f",energy)}",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start
                )

                Slider(
                    modifier = Modifier.padding(bottom = 4.dp),
                    value = energy,
                    onValueChange = {
                        energy = it
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = BackgroundWhite,
                        activeTrackColor = PrimaryBlack
                    )
                )
            }

            //Column for instrumentalness
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Instrumentalness: ${String.format("%.2f",instrumentalness)}",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start
                )

                Slider(
                    modifier = Modifier.padding(bottom = 4.dp),
                    value = instrumentalness,
                    onValueChange = {
                        instrumentalness = it
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = BackgroundWhite,
                        activeTrackColor = PrimaryBlack
                    )
                )
            }


            //Column for speechiness
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Speechiness: ${String.format("%.2f",speechiness)}",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start
                )

                Slider(
                    modifier = Modifier.padding(bottom = 4.dp),
                    value = speechiness,
                    onValueChange = {
                        speechiness = it
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = BackgroundWhite,
                        activeTrackColor = PrimaryBlack
                    )
                )
            }


            //The button for building
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment =  Alignment.BottomCenter
            ) {
                //Button to proceed to build the playlist
                Button(
                    onClick = {
                        viewModel.addParamsToPreferences(acousticness,dancebility,energy, instrumentalness, speechiness)
                        navController.navigate("fragmentConfirmParameters")
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(15)
                ) {
                    Text(
                        text = "Let's build it >",
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}