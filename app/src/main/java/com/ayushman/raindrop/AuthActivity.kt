package com.ayushman.raindrop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.authToken.AuthToken
import com.ayushman.raindrop.database.authToken.AuthTokenDao
import com.ayushman.raindrop.database.userData.UserDataDao
import com.ayushman.raindrop.repository.UserDataRepository
import com.ayushman.raindrop.ui.composables.newWeatherDialogBox
import com.ayushman.raindrop.ui.theme.RaindropTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import kotlinx.coroutines.*

@ExperimentalFoundationApi
class AuthActivity : ComponentActivity() {

    //Declaring the job and uiScope for the coroutines
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    //Declaring the Dao object
    private lateinit var authTokenDao: AuthTokenDao
    private lateinit var userDataDao : UserDataDao

    //String for holding the accessToken
    private lateinit var token: String

    //LiveData to observe if the data is added to LocalDataBase
    private val navigate = MutableLiveData(false)

    private var city = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialising the DAOs
        authTokenDao = LocalDatabase.getInstance(applicationContext).authTokenDao
        userDataDao = LocalDatabase.getInstance(applicationContext).userDataDao

        //The state for showing or closing dialog
        val openDialogChangeLocation =  mutableStateOf(false)

        setContent {
            RaindropTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    city = newWeatherDialogBox(
                        openDialog = openDialogChangeLocation,
                        onClicked = {
                            openDialogChangeLocation.value = false
                            authorize()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_login),
                            contentDescription = "Login illustration",
                            modifier = Modifier
                                .height(300.dp)
                                .width(300.dp)
                        )
                        Text(
                            text = "Personalised playlists based on your mood",
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Find the right choice of your music choice based on your mood and atmosphere for free",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            onClick = {
                                openDialogChangeLocation.value = true
                            },
                            modifier = Modifier
                                .padding(start = 25.dp, end = 25.dp, bottom = 30.dp)
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = RoundedCornerShape(15)
                        ) {
                            Text(
                                text = "Sign In",
                                style = MaterialTheme.typography.button
                            )
                        }
                    }
                }
            }
        }
    }

    //Receiving the intent after the user has logged in
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE)
        {
            val response = AuthorizationClient.getResponse(resultCode,data)
            when (response.type) {
                //Handling if the access token is received
                AuthorizationResponse.Type.TOKEN -> {
                    //Adding data to local database
                    token = response.accessToken
                    uiScope.launch {
                        addTokenToDatabase(response.accessToken) //Method to add the received accessToken to LocalDB
                        getUserData(city) //Get the SpotifyUserData using accessToken
                        navigate.value = true
                    }

                    //Observing the LiveData to navigate when the value is true
                    navigate.observe(this) {
                        if (it) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                //Handling any errors that may arise during the auth flow
                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(this,"An unexpected error occurred! ${response.error}",Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this,"An unexpected error occurred!",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Function called to authorize the user
    private fun authorize() {
        val builder = AuthorizationRequest.Builder(CLIENT_ID,
            AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(SCOPES)
        val request = builder.build()
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE,request)
    }

    //Function to add the accessToken to LocalDB
    private suspend fun addTokenToDatabase(accessToken : String) {
        val newAuthToken = AuthToken(access_token = accessToken)
        withContext(Dispatchers.IO) {
            authTokenDao.insert(newAuthToken)
        }
    }

    //Refreshing the userRepository in order to receive userData from Spotify
    private suspend fun getUserData(city:String) {
        val userDataRepository = UserDataRepository(LocalDatabase.getInstance(applicationContext))
        withContext(Dispatchers.IO) {
            userDataRepository.refreshUserData(city)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() //Preventing the memory leaks
    }
}

