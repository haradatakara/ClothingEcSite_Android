package com.example.clothingecsite_30.repository

import com.example.clothingecsite_30.model.authentication.login.LoggedInUser
import com.example.clothingecsite_30.model.authentication.register.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.example.clothingecsite_30.model.authentication.Result


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository() {

    private val fireAuth = Firebase.auth
    private val fireStore = Firebase.firestore


    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLogout: Boolean
        get() = user != null

    init {
        user = null
    }

    suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO) {
            user = null
            val userStatus = fireAuth.signOut()
            delay(100)
            return@withContext true
        }
    }

    suspend fun fetchLoginUser(): User? {
        val uid = fireAuth.uid

        return fireStore
            .collection("user")
            .document(uid!!)
            .get()
            .await()
            .toObject(User::class.java)

    }

    suspend fun login(username: String, password: String): Result<LoggedInUser>? {
        var authenticatedUser: Result<LoggedInUser>? = null
        try {
            fireAuth.signInWithEmailAndPassword(username, password).await()
            val user = fireAuth.currentUser
            if (user != null) {
                val uid: String = user.uid
                setLoggedInUser(LoggedInUser(uid, username, password))
                authenticatedUser = com.example.clothingecsite_30.model.authentication.Result.Success(LoggedInUser(uid, username, password))
            }
        } catch (e: java.lang.Exception) {
            authenticatedUser = Result.Error(e)
        }
        return authenticatedUser
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}