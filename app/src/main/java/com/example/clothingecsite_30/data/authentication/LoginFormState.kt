package com.example.clothingecsite_30.data.authentication

/**
 * ログイン時の入力エラーに関して扱うデータクラス
 */
data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)