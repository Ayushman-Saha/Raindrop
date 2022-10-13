package com.ayushman.raindrop.ui.fragments

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ayushman.raindrop.ui.composables.GenreGridView
import com.ayushman.raindrop.ui.theme.Black
import com.ayushman.raindrop.ui.theme.PrimaryBlack
import com.ayushman.raindrop.ui.theme.White
import com.ayushman.raindrop.ui.viewmodels.FragmentSelectGenreViewModel

@ExperimentalFoundationApi
@Composable
fun FragmentSelectGenre(context: Context, navController: NavController) {

    //States for search query and the focus manager
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    //Initialising the viewModel
    val viewModel : FragmentSelectGenreViewModel = viewModel(
        factory = FragmentSelectGenreViewModel.FragmentGenreViewModelFactory(context)
    )

    //Observing the livedata as a state for the composable
    val genreDataState = viewModel.genreData.observeAsState()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
    )  {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {

                //Text for showing the select genre header
                Text(
                    modifier = Modifier.padding(top = 32.dp, start = 4.dp),
                    text = "Select Genre",
                    style = MaterialTheme.typography.h1
                )

                //Text for showing the instructions
                Text(
                    modifier = Modifier.padding(start = 4.dp, bottom = 16.dp),
                    text = "Choose any one genre from the list",
                    style = MaterialTheme.typography.body1
                )
            }

            //The search box
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.refreshArtistDataOnQuery(it)
                },
                shape = RoundedCornerShape(20.dp),
                trailingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search icon", tint = PrimaryBlack)
                },
                label = {
                    Text(text = "Search for favourite genre",style = MaterialTheme.typography.body1)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = White,
                    textColor = Black,
                    unfocusedLabelColor = Color.Gray
                ),
                textStyle = MaterialTheme.typography.body1,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {focusManager.clearFocus()})
            )

            val gridState = rememberLazyListState()

            //The grid for the selection for the genres
            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                cells = GridCells.Fixed(2),
                state = gridState,
            ) {
                genreDataState.value?.let { mainList ->
                    items(mainList) { item->
                        GenreGridView(
                            genreData = item,
                            onClick = {
                               viewModel.updateToPreference(item)
                                navController.navigate("fragmentSelectTrack")
                            }
                        )
                    }
                }
            }
        }
    }

}