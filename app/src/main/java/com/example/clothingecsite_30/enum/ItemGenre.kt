package com.example.clothingecsite_30.enum

/**
 * アイテムジャンルを扱うenum
 */
enum class ItemGenre(val id: Int, val value: String) {
    ALL(1, "all"),
    TOPS(2, "tops"),
    PANTS(3, "pants"),
    SHOES(4, "shoes"),
}