package com.example.clothingecsite_30.model.authentication.login

/**
 * 　ログインユーザー情報を管理するモデルクラス
 */
data class LoggedInUser(
    val uuid: String,
    val username: String,
    val email: String
)