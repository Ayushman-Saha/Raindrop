package com.ayushman.raindrop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.MutableLiveData
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.authToken.AuthToken
import com.ayushman.raindrop.database.authToken.AuthTokenDao
import com.ayushman.raindrop.database.userData.DatabaseUserData
import com.ayushman.raindrop.database.userData.UserDataDao
import com.ayushman.raindrop.ui.theme.RaindropTheme
import kotlinx.coroutines.*

@ExperimentalFoundationApi
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    companion object {
        const val SPLASH_TIME = 1500L
    }

    //Declaring the UserData Dao and the userData objects
    private lateinit var userDataDao: UserDataDao
    private var databaseUser  : DatabaseUserData? = null

    //Declaring the UserData Dao and the userData objects
    private lateinit var authTokenDao: AuthTokenDao
    private var authToken  : AuthToken? = null

    //Declaring the job and uiScope for the coroutines
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    //LiveData to observe if the data is received from the local database
    private val navigate = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //Initialising the UserData Dao
        userDataDao = LocalDatabase.getInstance(applicationContext).userDataDao

        //Initialising the UserData Dao
        authTokenDao = LocalDatabase.getInstance(applicationContext).authTokenDao

        setContent {
            RaindropTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Row (
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                            ){
                        Image(
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = "Default logo of the app"
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        //Getting user data from local database on background thread
        uiScope.launch {
            getUser()
            getAuthToken()
            delay(SPLASH_TIME)
            navigate.value = true
        }

        //Observing the navigation LiveData and navigate if the data changes to true
        navigate.observe(this) { navigate ->
            val intent: Intent
            if (navigate) {
                intent = if (databaseUser != null && authToken != null) {
                    //Calculating the time elapsed after the token is requested
                    val diff = System.currentTimeMillis() - authToken!!.time
                    val diffHour = (diff / (1000 * 60 * 60)).toDouble()
                    //If token expires -> Do a refresh
                    if (diffHour >= 1)
                        Intent(this, RefreshTokenActivity::class.java)
                    else //Move to MainActivity
                        Intent(this, MainActivity::class.java)
                } else //Prompt the user to login
                    Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Cancelling the job to avoid memory leaks
        job.cancel()
    }

    //Function to fetch userData from the local database
    private suspend fun getUser() {
        withContext(Dispatchers.IO) {
            databaseUser = userDataDao.get()
        }
    }

    private suspend fun getAuthToken() {
        withContext(Dispatchers.IO) {
            authToken = authTokenDao.get()
        }
    }
}