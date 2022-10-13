package com.ayushman.raindrop.ui.fragments

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ayushman.raindrop.R
import com.ayushman.raindrop.ui.composables.newWeatherDialogBox
import com.ayushman.raindrop.ui.viewmodels.FragmentWeatherHomeViewModel
import java.util.*

@ExperimentalFoundationApi
@Composable
fun FragmentWeatherHome(context : Context, navController: NavController) {

    //State for showing/closing of AlertDialog for change location
    val openDialogChangeLocation = remember { mutableStateOf(false) }

    //The variable for storing the new city
    var newCity = ""

    //Setting up the viewModel
    val viewModel : FragmentWeatherHomeViewModel = viewModel(
        factory = FragmentWeatherHomeViewModel.FragmentWeatherViewModelFactory(context)
    )

    //Getting the state from live data
    val state = viewModel.weatherData.observeAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        //Text for showing the weather header
        Text(
            modifier = Modifier.padding(24.dp),
            text = "Weather",
            style = MaterialTheme.typography.h1
        )

        //Row for shifting the logout and location buttons to end
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.height(110.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {

                //Change location button
                Button(
                    modifier = Modifier
                        .height(50.dp),
                    onClick = {
                        openDialogChangeLocation.value = true
                    },
                    shape = RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp)
                ) {
                    Text(
                        text = "Change Location",
                        style = MaterialTheme.typography.button
                    )
                }

                //Button for logging out
                Button(
                    modifier = Modifier
                        .height(50.dp),
                    onClick = {
                        navController.navigate("fragmentUserProfile")
                    },
                    shape = RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp)
                ) {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }


        //The change location dialog
        newCity = newWeatherDialogBox(openDialog = openDialogChangeLocation, onClicked = {
            openDialogChangeLocation.value = false
            viewModel.changeLocation(newCity)
        })

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //The illustration for the weather
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(id = sendIllustrations(state.value?.weatherId.toString())),
                contentDescription = "Weather illustration")

            //The temperature displaying text
            Text(
                text = "${state.value?.temperature?.toInt()}℃",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center
            )

            //The temperature description text
            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp),
                text = "${state.value?.weatherDesc?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ENGLISH
                    ) else it.toString()
                }}",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center
            )

            //The location text
            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 36.dp),
                text = "${state.value?.cityName}, ${state.value?.countryName}",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )

            //Button to proceed to build the playlist
            Button(
                onClick = {
                    viewModel.clearPreference()
                    navController.navigate("fragmentSelectArtists")
                },
                modifier = Modifier
                    .padding(start = 25.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(15)
            ) {
                Text(
                    text = "Let's build your playlist >",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

//The function returns the illustration id based on the weather code
fun sendIllustrations(code : String) : Int {

    var drawable = R.drawable.ic_logo

    if (code.startsWith('2') || code.startsWith('3') || code.startsWith('5'))
        drawable = R.drawable.ic_rainy
    else if (code.startsWith('6'))
        drawable = R.drawable.ic_snow
    else if (code.startsWith('7'))
        drawable = R.drawable.ic_hazy
    else if (code == "800")
        drawable = R.drawable.ic_sunny
    else if (code.startsWith("80"))
        drawable = R.drawable.ic_cloudy

    return drawable
}
