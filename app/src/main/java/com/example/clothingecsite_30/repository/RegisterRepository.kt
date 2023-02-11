package com.example.clothingecsite_30.repository

import androidx.core.net.toUri
import com.example.clothingecsite_30.model.authentication.login.LoggedInUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.example.clothingecsite_30.model.authentication.Result
import com.example.clothingecsite_30.model.authentication.register.RegisterUser
import com.google.firebase.storage.ktx.storage

/**
 * 会員登録を扱うレポジトリクラス
 */
class RegisterRepository {

    private val fireAuth = Firebase.auth
    private val fireStore = Firebase.firestore
    private val fireStorage = Firebase.storage
    private val storageRef = fireStorage.reference.child("images/user-images")

    var user: LoggedInUser? = null
        private set

    init {
        user = null
    }

    // ユーザー作成
    suspend fun register(userInfo: RegisterUser): Result<LoggedInUser> {
        return try {
            fireAuth
                .createUserWithEmailAndPassword(userInfo.email, userInfo.password)
                .await()
            val user = fireAuth.currentUser
            if (user != null) {
                // 画像登録(storage)
                if(!userInfo.image.isNullOrBlank()) {
                    registerImage(userInfo.image!!, user.uid)
                }
                //個人情報登録(fireStore)
                registerDetail(userInfo)
                Result.Success(LoggedInUser(user.uid, userInfo.email, userInfo.name))
            } else {
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    //プロフィール情報を保存
    private suspend fun registerImage(imgUri: String, uid: String) {
        //画像登録
        storageRef
            .child("${uid}.png")
            .putFile(imgUri.toUri())
            .await()
    }

    // ユーザー詳細保存
    private suspend fun registerDetail(
        userInfo: RegisterUser
    ) {
        fireStore.collection("user").document(fireAuth.uid!!).set(
            hashMapOf(
                "image" to "${fireAuth.uid}.png",
                "name" to userInfo.name,
                "email" to userInfo.email,
                "address" to userInfo.zipcode,
                "prefecture" to userInfo.prefectureCity,
                "mansion" to userInfo.mansion,
                "birth" to userInfo.birth,
                "gender" to userInfo.gender
            )
        ).await()

        setLoggedInUser(
            LoggedInUser(
                fireAuth.uid!!, userInfo.email, userInfo.password
            )
        )
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

}