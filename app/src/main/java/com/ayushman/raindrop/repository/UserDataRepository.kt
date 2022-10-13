package com.ayushman.raindrop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ayushman.raindrop.database.LocalDatabase
import com.ayushman.raindrop.database.userData.asDomainModel
import com.ayushman.raindrop.domain.UserData
import com.ayushman.raindrop.network.SpotifyApi
import com.ayushman.raindrop.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataRepository (private val localDatabase  : LocalDatabase) {

    //The userData observable to be changed if any changes occurs in the LocalDB
    val userData : LiveData<List<UserData>> = Transformations.map(localDatabase.userDataDao.getUserDataObservable()) {
        it.asDomainModel()
    }

    //Method to refresh the userData in LocalDatabase from the network call
    suspend fun refreshUserData(cityName : String) {
        val accessToken = getToken()
        //Adding the auth Header to the call
        val header  = mapOf(
            "Authorization" to "Bearer $accessToken"
        )
        withContext(Dispatchers.IO) {
            try {
                //Fetching the userData from spotify API
                val userData = SpotifyApi.spotifyService.getUserDataAsync(header).await()
                //Mapping the network object to Database Object and inserting on the table
                localDatabase.userDataDao.insert(userData.asDatabaseModel(cityName))
            } catch (t : Throwable) {
                //Adding the error flag if the HTTP request fails
                val userDataFromDatabase = localDatabase.userDataDao.get()
                userDataFromDatabase!!.isError  = true
                localDatabase.userDataDao.insert(userDataFromDatabase)
            }
        }
    }

    //Function to return the access token from the local database
    suspend fun getToken() : String {
        var accessToken  : String
        withContext(Dispatchers.IO) {
            val authToken = localDatabase.authTokenDao.get()
            accessToken = authToken?.access_token!!
        }
        return accessToken
    }
}