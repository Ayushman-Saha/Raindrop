package com.ayushman.raindrop.ui.fragments

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.navigation.NavHostController
import com.ayushman.raindrop.R
import com.ayushman.raindrop.ui.composables.TrackListItems
import com.ayushman.raindrop.ui.theme.BackgroundWhite
import com.ayushman.raindrop.ui.theme.Black
import com.ayushman.raindrop.ui.theme.PrimaryBlack
import com.ayushman.raindrop.ui.theme.White
import com.ayushman.raindrop.ui.viewmodels.FragmentSelectTracksViewModel
import kotlinx.coroutines.launch

@Composable
fun FragmentSelectTrack(context: Context, navController: NavHostController) {

    //States for search query and the focus manager
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    //Initialising the viewModel
    val viewModel : FragmentSelectTracksViewModel = viewModel(
        factory = FragmentSelectTracksViewModel.FragmentTrackViewModelFactory(context)
    )

    //Observing the livedata as state
    val trackDataSearchState = viewModel.trackData.observeAsState()
    val selectedTrackDataState = viewModel.selectedTrackData.observeAsState()

    //States for the snackbar
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = {
            //Setting up theme for snackbar
            SnackbarHost(hostState = it) {data ->
                Snackbar(
                    snackbarData = data,
                    backgroundColor = BackgroundWhite,
                    actionColor = PrimaryBlack
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                backgroundColor = Black,
                text = { Text(text = "Done", style = MaterialTheme.typography.button) },
                icon = {
                    Icon(
                        Icons.Filled.Check,
                        tint = White,
                        contentDescription = "Check icon"
                    )
                },
                onClick = {
                    //Functionality for the done button
                    if (selectedTrackDataState.value?.size != 2) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Please select exactly 2 tracks to continue",
                                duration = SnackbarDuration.Short
                            )
                        }
                    } else {
                        viewModel.updateToPreference(
                            track1 = selectedTrackDataState.value!![0],
                            track2 = selectedTrackDataState.value!![1]
                        )
                        navController.navigate("fragmentAdjustParams")
                    }
                }
            )
        }
    ) {

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
                    text = "Select Track",
                    style = MaterialTheme.typography.h1
                )

                //Text for showing the instructions
                Text(
                    modifier = Modifier.padding(start = 4.dp, bottom = 16.dp),
                    text = "Search and select any 2 tracks",
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
                    viewModel.refreshTracksOnQuery(it)
                },
                shape = RoundedCornerShape(20.dp),
                trailingIcon = {
                    IconButton(
                       onClick = {
                           viewModel.queryTracks(searchQuery)
                       }
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search icon",
                            tint = PrimaryBlack
                        )
                    }
                },
                label = {
                    Text(
                        text = "Search for favourite tracks",
                        style = MaterialTheme.typography.body1
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = White,
                    textColor = Black,
                    unfocusedLabelColor = Color.Gray
                ),
                textStyle = MaterialTheme.typography.body1,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() })

            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (searchQuery != "")
                    Text(
                        text = "Search results :",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Start
                    )
                else
                    Text(
                        text = "Selected Tracks :",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Start
                    )
            }

            val listState = rememberLazyListState()

            if (searchQuery != "")
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    state = listState
                ) {
                    trackDataSearchState.value?.let {
                        items(it) {item ->

                            //State for the individual items
                            val checkedState = mutableStateOf(item.isSelected)

                            TrackListItems(
                                trackData = item,
                                checkedState = checkedState.value,
                                onClick = {
                                    checkedState.value = !checkedState.value
                                    searchQuery = ""
                                    if (checkedState.value)
                                        viewModel.modifySelectedList(item,true,searchQuery)
                                    else
                                        viewModel.modifySelectedList(item,false,searchQuery)
                                }
                            )
                        }
                    }
                }
            else {
                if (selectedTrackDataState.value != null && selectedTrackDataState.value!!.isNotEmpty())
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        state = listState
                    ) {
                        selectedTrackDataState.value?.let {
                            items(it) {item ->

                                //State for the individual items
                                val checkedState = mutableStateOf(item.isSelected)

                                TrackListItems(
                                    trackData = item,
                                    checkedState = checkedState.value,
                                    onClick = {
                                        checkedState.value = !checkedState.value
                                        if (checkedState.value)
                                            viewModel.modifySelectedList(item,true,searchQuery)
                                        else
                                            viewModel.modifySelectedList(item,false,searchQuery)
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
                            text = "Looks like you haven't searched and selected any track!",
                            style = MaterialTheme.typography.subtitle1,
                            textAlign = TextAlign.Center
                        )
                    }
            }

        }
    }
}