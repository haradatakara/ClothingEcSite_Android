package com.example.clothingecsite_30.repository

import com.example.clothingecsite_30.model.authentication.login.LoggedInUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.example.clothingecsite_30.model.authentication.Result

class RegisterRepository {

    private val fireAuth = Firebase.auth
    private val fireStore = Firebase.firestore

    private lateinit var result: Result<LoggedInUser>

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLogout: Boolean
        get() = user != null

    init {
        user = null
    }

    suspend fun register(userInfo: HashMap<String, String>): Result<LoggedInUser>? {
        var authenticatedUser: Result<LoggedInUser>? = null
        try {
            fireAuth.createUserWithEmailAndPassword(userInfo["email"]!!, userInfo["password"]!!)
                .await()
            val user = fireAuth.currentUser
            if (user != null) {
                val uid: String = user.uid
                registerDetail(userInfo)
                setLoggedInUser(
                    LoggedInUser(
                        fireAuth.uid!!,
                        userInfo["email"]!!,
                        userInfo["password"]!!
                    )
                )
                authenticatedUser =
                    Result.Success(LoggedInUser(uid, userInfo["email"]!!, userInfo["password"]!!))
            } else {
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            authenticatedUser = Result.Error(e)
        }
        return authenticatedUser
    }

    private suspend fun registerDetail(
        userInfo: HashMap<String, String>
    ) {
        fireStore
            .collection("user")
            .document(fireAuth.uid!!)
            .set(hashMapOf(
                "name" to userInfo["username"],
                "email" to userInfo["email"],
                "address" to userInfo["address"],
                "birth" to userInfo["birth"],
                "gender" to userInfo["gender"]
            )).await()
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

}