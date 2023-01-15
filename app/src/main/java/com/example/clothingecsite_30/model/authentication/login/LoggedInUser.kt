package com.example.clothingecsite_30.model.authentication.login

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val uuid: String,
    val username: String,
    val email: String
)