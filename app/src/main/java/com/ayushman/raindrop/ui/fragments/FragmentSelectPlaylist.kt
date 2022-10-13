package com.ayushman.raindrop.ui.fragments

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ayushman.raindrop.R
import com.ayushman.raindrop.ui.composables.PlaylistGridView
import com.ayushman.raindrop.ui.composables.newPlaylistCreateDialog
import com.ayushman.raindrop.ui.theme.Black
import com.ayushman.raindrop.ui.theme.PrimaryBlack
import com.ayushman.raindrop.ui.theme.White
import com.ayushman.raindrop.ui.viewmodels.FragmentSelectPlaylistViewModel

@ExperimentalFoundationApi
@Composable
fun FragmentSelectPlaylist(context: Context, navController: NavController, trackId : String) {

    //States for search query and the focus manager
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    var playListName by remember{ mutableStateOf("") }

    //Initialising the viewModel
    val viewModel : FragmentSelectPlaylistViewModel = viewModel(
        factory = FragmentSelectPlaylistViewModel.FragmentSelectPlaylistViewModelFactory(context)
    )

    //Observing the livedata as a state for the composable
    val playlistDataState = viewModel.playlistData.observeAsState()
    var openDialog by remember { mutableStateOf(false) }


    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                backgroundColor = Black,
                text = { Text(text = "Add Playlist", style = MaterialTheme.typography.button) },
                icon = {Icon(Icons.Filled.Add, tint = White, contentDescription = "Check icon")},
                onClick = {
                    //Functionality for the done button
                    openDialog = true
                }
            )
        })
    {

        playListName = newPlaylistCreateDialog(
            openDialog = openDialog,
            onClicked = {
                if(playListName!="") {
                    viewModel.addNewPlaylist(playListName)
                    Toast.makeText(context, "New playlist $playListName created", Toast.LENGTH_LONG)
                        .show()
                    openDialog =false
                } else {
                    Toast.makeText(context, "Playlist name can't be blank", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            onDismissClick = {
                openDialog = false
            }
        )

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

                //Text for showing the select playlists header
                Text(
                    modifier = Modifier.padding(top = 32.dp, start = 4.dp),
                    text = "Select Playlist",
                    style = MaterialTheme.typography.h1
                )

                //Text for showing the instructions
                Text(
                    modifier = Modifier.padding(start = 4.dp, bottom = 16.dp),
                    text = "Choose playlist to add the track",
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
                    viewModel.refreshPlaylistDataOnQuery(it)
                },
                shape = RoundedCornerShape(20.dp),
                trailingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search icon", tint = PrimaryBlack)
                },
                label = {
                    Text(text = "Search for your playlists",style = MaterialTheme.typography.body1)
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

            if (playlistDataState.value?.isNotEmpty() == true)
            //The grid for the selection for the artists
                LazyVerticalGrid(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    cells = GridCells.Fixed(2),
                    state = gridState,
                ) {
                    playlistDataState.value?.let { mainList ->
                        items(mainList) { item->
                            PlaylistGridView(
                                playlist = item ,
                                onClick = {
                                    viewModel.addTrackToPlaylist(
                                        playlistId = item.playlistId,
                                        trackUri = trackId
                                    )
                                    Toast.makeText(context, "Track added to the playlist successfully!",Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            else
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .height(200.dp)
                            .width(200.dp),
                        painter = painterResource(id = R.drawable.ic_empty_state),
                        contentDescription = "Empty state illustration"
                    )

                    Text(
                        text = "Looks like you haven't created any playlist! Use the add button to create a new playlist",
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Center
                    )
                }
        }
    }

}