package com.example.clothingecsite_30.model

/**
 * 住所に関してまとめるモデルクラス
 */
data class Address(
    val zipcode: Int,
    val prefecture: String,
    val city: String,
    val address: String
)