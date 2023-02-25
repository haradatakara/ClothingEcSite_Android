package com.example.clothingecsite_30.model.authentication

data class UpdateUser(
    var image: String?,
    var name: String,
    var email: String = "",
)
