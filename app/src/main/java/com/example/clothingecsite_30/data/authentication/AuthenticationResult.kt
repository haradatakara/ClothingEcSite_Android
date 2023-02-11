package com.example.clothingecsite_30.data.authentication

/**
 *　認証に関する結果を扱うデータクラス
 */
data class AuthenticationResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)