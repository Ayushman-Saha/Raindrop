package com.ayushman.raindrop.ui.fragments

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ayushman.raindrop.ui.composables.RecommendedListItems
import com.ayushman.raindrop.ui.theme.Black
import com.ayushman.raindrop.ui.theme.PrimaryBlack
import com.ayushman.raindrop.ui.theme.White
import com.ayushman.raindrop.ui.viewmodels.FragmentRecommendationViewModel

@Composable
fun fragmentRecommendationResult (context: Context, navController: NavController) : MediaPlayer {

    //States for search query and the focus manager
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    //Initialising the viewModel
    val viewModel : FragmentRecommendationViewModel = viewModel(
        factory = FragmentRecommendationViewModel.FragmentRecommendationViewModelFactory(context)
    )

    //Initialising the mediaPlayer
    val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    //Observing the state
    val recommendationTrackState = viewModel.recommendationData.observeAsState()
    val previewTrackStateState = viewModel.previewTrack.observeAsState()

   Scaffold {

       Box() {

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
                       text = "Your personalised results",
                       style = MaterialTheme.typography.h1
                   )

                   //Text for showing the instructions
                   Text(
                       modifier = Modifier.padding(start = 4.dp, bottom = 16.dp),
                       text = "Click on play button for a preview audio and tap the items to add it to a playlist",
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
                       viewModel.refreshRecommendationListOnQuery(searchQuery)
                   },
                   shape = RoundedCornerShape(20.dp),
                   trailingIcon = {
                       Icon(
                           Icons.Filled.Search,
                           contentDescription = "Search icon",
                           tint = PrimaryBlack
                       )
                   },
                   label = {
                       Text(
                           text = "Search for recommended tracks",
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
                   keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                   keyboardActions = KeyboardActions(
                       onDone = { focusManager.clearFocus() })

               )

               Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(16.dp),
                   contentAlignment = Alignment.CenterStart
               ) {
                   Text(
                       text = "Search results :",
                       style = MaterialTheme.typography.body2,
                       textAlign = TextAlign.Start
                   )
               }

               val listState = rememberLazyListState()


               LazyColumn(
                   modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                   state = listState
               ) {
                   recommendationTrackState.value?.let {
                       items(it) {item ->

                           RecommendedListItems(
                               trackData = item,
                               mediaState = false,
                               isPreviewUrlPresent = item.previewUrl != "",
                               onClickAdd = {
                                   mediaPlayer.reset()
                                   viewModel.changePreviewTrack(null)
                                   navController.navigate("fragmentSelectPlaylist/${item.trackUid}")
                               },
                               onClickMedia = {
                                   viewModel.changePreviewTrack(item)
                                   mediaPlayer.apply {
                                       reset()
                                       setDataSource(item.previewUrl)
                                       prepare()
                                       start()
                                   }
                               }
                           )
                       }
                   }
               }
           }

           if(previewTrackStateState.value != null){

               var mediaState by remember { mutableStateOf(true)}

               Box(
                   modifier = Modifier.fillMaxWidth()
                       .height(120.dp)
                       .padding(16.dp),
                   contentAlignment = Alignment.BottomCenter
               ) {
                   RecommendedListItems(
                       trackData = previewTrackStateState.value!!,
                       onClickAdd = {},
                       isPreviewUrlPresent = true,
                       mediaState = mediaState,
                       onClickMedia = {
                           mediaState = !mediaState
                           if (mediaState)
                               mediaPlayer.start()
                           else
                               mediaPlayer.pause()

                           mediaPlayer.setOnCompletionListener {
                               mediaState = false
                               viewModel.changePreviewTrack(null)
                           }

                           mediaPlayer.setOnErrorListener { _, _, _ ->
                               mediaState = false
                               Toast.makeText(context, "Failed to receive preview audio from the source",Toast.LENGTH_LONG).show()
                               return@setOnErrorListener true
                           }
                       },
                   )
               }
           }
       }
    }

    return mediaPlayer
}