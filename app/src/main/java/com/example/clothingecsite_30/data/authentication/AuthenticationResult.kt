package com.example.clothingecsite_30.data.authentication

/**
 * Authentication result : success (user details) or error message.
 */
data class AuthenticationResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)