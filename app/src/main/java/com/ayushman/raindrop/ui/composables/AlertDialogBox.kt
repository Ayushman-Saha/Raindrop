package com.ayushman.raindrop.ui.composables

import android.widget.Toast

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun LogoutDialog(openDialog : MutableState<Boolean>, onClick: ()-> Unit) {

    if (openDialog.value)
    {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Logout Alert", style = MaterialTheme.typography.h2) },
            text = { Text(text = "Are you sure you want to logout?", style = MaterialTheme.typography.body1) },

            confirmButton = {

                TextButton(
                    onClick = onClick
                ) {
                    Text(text = "Confirm", style = MaterialTheme.typography.subtitle1)
                }

            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(text = "Dismiss", style = MaterialTheme.typography.subtitle1)
                }
            },
        )
    }
}

//The composable for getting new weather city
@Composable
fun newWeatherDialogBox(openDialog : MutableState<Boolean>, onClicked : () -> Unit) : String {

    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                Toast.makeText(context,"A location is necessary to continue further",Toast.LENGTH_LONG).show()
            },
            title = {
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.subtitle1,
                )
            },
            text = {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text(text = "City Name", style = MaterialTheme.typography.body1)
                    }
                )
            },
            confirmButton = {

                TextButton(
                    onClick = onClicked
                ) {
                    Text(text = "Confirm", style = MaterialTheme.typography.subtitle1)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        Toast.makeText(context,"A location is necessary to continue further",Toast.LENGTH_LONG).show()
                    }) {
                    Text(text = "Dismiss", style = MaterialTheme.typography.subtitle1)
                }
            },

            )
    }
    return text
}


//The composable for getting new playlist name
@Composable
fun newPlaylistCreateDialog(openDialog : Boolean, onClicked : () -> Unit, onDismissClick : () -> Unit) : String {

    var text by remember { mutableStateOf("") }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Playlist Name",
                    style = MaterialTheme.typography.subtitle1,
                )
            },
            text = {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text(text = "Playlist name", style = MaterialTheme.typography.body1)
                    }
                )
            },
            confirmButton = {

                TextButton(
                    onClick = onClicked
                ) {
                    Text(text = "Confirm", style = MaterialTheme.typography.subtitle1)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissClick
                ) {
                    Text(text = "Dismiss", style = MaterialTheme.typography.subtitle1)
                }
            },

            )
    }
    return text
}