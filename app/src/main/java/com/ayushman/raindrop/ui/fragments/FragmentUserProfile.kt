package com.ayushman.raindrop.ui.fragments

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.ayushman.raindrop.AuthActivity
import com.ayushman.raindrop.ui.composables.LogoutDialog
import com.ayushman.raindrop.ui.viewmodels.FragmentUserProfileViewModel
import com.spotify.sdk.android.auth.AuthorizationClient

@ExperimentalFoundationApi
@Composable
fun FragmentUserProfile (context: Context) {

    val viewModel : FragmentUserProfileViewModel = viewModel(
        factory = FragmentUserProfileViewModel.FragmentUserProfileViewModelFactory(context)
    )

    val userData by viewModel.userData.observeAsState()
    val playlistData by viewModel.playlistData.observeAsState()

    val openDialogLogout = remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(32.dp)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
                    .clip(RoundedCornerShape(10)),
                painter = rememberImagePainter(userData?.get(0)?.displayPicture),
                contentDescription = "image"
            )
            Text(
                text = userData?.get(0)?.displayName.toString(),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = userData?.get(0)?.email.toString(),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            //The logout dialog box
            LogoutDialog(
                openDialog = openDialogLogout,
                onClick = {
                    openDialogLogout.value = false

                    viewModel.clearData()

                    //Logging out
                    AuthorizationClient.clearCookies(context)
                    Toast.makeText(context, "Successfully logged out!", Toast.LENGTH_LONG).show()

                    //Switching activities
                    val intent = Intent(context, AuthActivity::class.java)
                    ContextCompat.startActivity(context, intent, null)
                }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Followers",
                        style = MaterialTheme.typography.h2
                    )
                    Text(
                        text = userData?.get(0)?.followers.toString(),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Playlist",
                        style = MaterialTheme.typography.h2
                    )

                    Text(
                        text = playlistData?.size.toString(),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
            Button(
                onClick = {
                    openDialogLogout.value = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 50.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.button
                )
            }

        }

    }
}