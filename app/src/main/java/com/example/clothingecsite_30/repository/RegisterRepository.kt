package com.example.clothingecsite_30.repository

import androidx.core.net.toUri
import com.example.clothingecsite_30.model.authentication.login.LoggedInUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.example.clothingecsite_30.model.authentication.Result
import com.google.firebase.storage.ktx.storage

class RegisterRepository {

    private val fireAuth = Firebase.auth
    private val fireStore = Firebase.firestore
    private val fireStorage = Firebase.storage
    private val storageRef = fireStorage.reference.child("images/user-images")

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLogout: Boolean
        get() = user != null

    init {
        user = null
    }

    suspend fun register(userInfo: HashMap<String, String>): Result<LoggedInUser> {
        return try {
            fireAuth.createUserWithEmailAndPassword(userInfo["email"]!!, userInfo["password"]!!)
                .await()
            val user = fireAuth.currentUser
            if (user != null) {
                //画像登録(storage)
                registerImage(userInfo["image"]!!, user.uid)
                //個人情報登録(fireStore)
                registerDetail(userInfo)
                Result.Success(LoggedInUser(user.uid, userInfo["email"]!!, userInfo["password"]!!))
            } else {
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun registerImage(imgUri: String, uid: String) {
        //画像登録
        storageRef
            .child("${uid}.png")
            .putFile(imgUri.toUri())
            .await()
    }

    private suspend fun registerDetail(
        userInfo: HashMap<String, String>
    ) {
        fireStore.collection("user").document(fireAuth.uid!!).set(
            hashMapOf(
                "image" to "${fireAuth.uid}.png",
                "name" to userInfo["username"],
                "email" to userInfo["email"],
                "address" to userInfo["address"],
                "birth" to userInfo["birth"],
                "gender" to userInfo["gender"]
            )
        ).await()

        setLoggedInUser(
            LoggedInUser(
                fireAuth.uid!!, userInfo["email"]!!, userInfo["password"]!!
            )
        )
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

}