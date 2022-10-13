package com.ayushman.raindrop.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ayushman.raindrop.domain.RecommendationTrackData
import com.ayushman.raindrop.domain.TrackData
import com.ayushman.raindrop.ui.theme.BackgroundWhite
import com.ayushman.raindrop.ui.theme.PrimaryBlack

@Composable
fun TrackListItems(
    trackData: TrackData,
    checkedState : Boolean,
    onClick : () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(
                onClick = onClick
            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Image(
                modifier = Modifier
                    .size(60.dp)
                    .fillMaxWidth(0.2F)
                    .clip(RoundedCornerShape(10.dp)),
                painter = rememberImagePainter(
                    trackData.trackCover,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Image of track album"
            )

            Column (
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(0.9F),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = trackData.trackName,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = trackData.trackArtist,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Start
                )
            }

            Checkbox(
                modifier = Modifier.fillMaxWidth(0.2F),
                checked = checkedState,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkmarkColor = BackgroundWhite,
                    checkedColor = PrimaryBlack,
                    uncheckedColor = PrimaryBlack
                )
            )
        }
    }
    
}

@Composable
fun RecommendedListItems(
    trackData: RecommendationTrackData,
    onClickAdd : () -> Unit,
    onClickMedia : () -> Unit,
    isPreviewUrlPresent : Boolean,
    mediaState : Boolean
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(
                onClick = onClickAdd
            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Image(
                modifier = Modifier
                    .size(60.dp)
                    .fillMaxWidth(0.2F)
                    .clip(RoundedCornerShape(10.dp)),
                painter = rememberImagePainter(
                    trackData.trackCover,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Image of track album"
            )

            Column (
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(0.85F),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = trackData.trackName,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = trackData.trackArtist,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Start
                )
            }

            if(isPreviewUrlPresent)
                IconButton(
                    onClick = onClickMedia,
                    enabled = isPreviewUrlPresent
                ) {
                    if (mediaState)
                        Icon(
                            Icons.Filled.PauseCircleFilled,
                            modifier = Modifier.size(40.dp),
                            contentDescription = "Pause button",
                            tint = PrimaryBlack
                        )
                    else
                        Icon(
                            Icons.Filled.PlayCircleFilled,
                            modifier = Modifier.size(40.dp),
                            contentDescription = "Play button",
                            tint = PrimaryBlack
                        )
                }
        }

    }
}


