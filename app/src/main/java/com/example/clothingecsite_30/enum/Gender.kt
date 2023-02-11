package com.example.clothingecsite_30.enum

/**
 * 性別に関するenum
 */
enum class Gender(val id: Int, val value: String) {
    MAN(1, "男性"),
    WOMAN(2, "女性"),
    OTHER(3, "その他")
}