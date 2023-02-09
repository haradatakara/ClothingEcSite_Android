package com.example.clothingecsite_30.model.authentication.register

/**
 * Userを管理するdataクラスです
 */
data class User(
    var image: String = "",
    val gender: String = "",
    val birth: String = "",
    val email: String = "",
    val address: String = "",
    val prefectureCity: String = "",
    val mansion: String = "",
    val name: String = ""
)
