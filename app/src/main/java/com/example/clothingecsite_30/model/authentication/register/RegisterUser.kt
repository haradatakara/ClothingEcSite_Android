package com.example.clothingecsite_30.model.authentication.register


/**
 * 登録するユーザー情報を管理するdataクラスです
 */
data class RegisterUser(
    var image: String?,
    var gender: String = "",
    var birth: String = "",
    var email: String = "",
    var zipcode: String = "",
    var prefectureCity: String = "",
    var mansion: String? = "",
    var name: String,
    var password: String = ""
)