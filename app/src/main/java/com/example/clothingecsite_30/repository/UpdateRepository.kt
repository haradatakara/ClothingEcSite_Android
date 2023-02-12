package com.example.clothingecsite_30.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.core.net.toUri
import com.example.clothingecsite_30.model.authentication.login.LoggedInUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.example.clothingecsite_30.model.authentication.Result
import com.example.clothingecsite_30.model.authentication.UpdateUser
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.storage.ktx.storage

/**
 * 会員登録を扱うレポジトリクラス
 */
class UpdateRepository {

    private val fireAuth = Firebase.auth
    private val fireStore = Firebase.firestore
    private val fireStorage = Firebase.storage
    private val storageRef = fireStorage.reference.child("images/user-images")
    private val user = fireAuth.currentUser

    // ユーザー作成
    suspend fun update(
        userInfo: UpdateUser,
        confirmPass: String,
        confirmEmail: String
    ): Result<LoggedInUser> {
        return try {
            val credential = EmailAuthProvider.getCredential(confirmEmail, confirmPass)
            user!!
                .reauthenticate(credential)
                .addOnSuccessListener { Log.d(TAG, "User success") }
                .await()

            //メールアドレス更新
            updateAuth(userInfo.email)

            // 画像登録(storage)
            if (!userInfo.image.isNullOrBlank()) {
                updateImage(userInfo.image!!, user.uid)
            }
            //個人情報登録(fireStore)
            registerDetail(userInfo)

            //成功したら
            Result.Success(LoggedInUser(user.uid, userInfo.email, userInfo.name))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    //プロフィール情報を保存
    private suspend fun updateImage(imgUri: String, uid: String) {
        storageRef
            .child("${uid}.png")
            .putFile(imgUri.toUri()).await()
    }

    //プロフィール情報を保存
    private suspend fun updateAuth(email: String) {
        user!!.updateEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Success")
            }
            .addOnFailureListener {
                println(it.message)
            }
            .await()
    }

    // ユーザー詳細保存
    private suspend fun registerDetail(
        updateUser: UpdateUser
    ) {
        fireStore.collection("user").document(fireAuth.uid!!).update(
            hashMapOf(
                "image" to "${fireAuth.uid}.png",
                "name" to updateUser.name,
                "email" to updateUser.email,
            ) as Map<String, Any>
        ).await()
    }
}