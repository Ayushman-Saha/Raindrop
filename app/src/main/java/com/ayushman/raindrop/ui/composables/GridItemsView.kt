package com.ayushman.raindrop.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.ayushman.raindrop.domain.ArtistData
import com.ayushman.raindrop.domain.GenreData
import com.ayushman.raindrop.domain.Playlist
import com.ayushman.raindrop.ui.theme.BackgroundWhite
import com.ayushman.raindrop.ui.theme.PrimaryBlack
import com.ayushman.raindrop.ui.theme.White

@Composable
fun ArtistGridView(artistData : ArtistData, checkedState : Boolean, onClick : ()->Unit) {

    Surface(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
            .padding(8.dp)
            .clickable(
                onClick = onClick
            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    painter = rememberImagePainter(
                        artistData.artistImage,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Image of artists"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black,
                                ),
                                startY = 300F
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        text = artistData.artistName,
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = White
                        ),
                        textAlign = TextAlign.Start
                    )
                }
            }

            Checkbox(
                modifier = Modifier.padding(8.dp),
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
fun GenreGridView(
    genreData : GenreData,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(PrimaryBlack)
            .clickable(
                onClick = onClick
            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxSize()
                .background(PrimaryBlack),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
            ){

            Text(
                text = genreData.genre,
                style = MaterialTheme.typography.button,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PlaylistGridView(playlist: Playlist, onClick : ()->Unit) {

    Surface(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
            .padding(8.dp)
            .clickable(
                onClick = onClick
            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    painter = rememberImagePainter(
                        playlist.playlistCover,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Image of artists"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black,
                                ),
                                startY = 300F
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        text = playlist.playlistName,
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = White,
                            fontSize = 14.sp
                        ),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}