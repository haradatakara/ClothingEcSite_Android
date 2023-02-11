package com.example.clothingecsite_30.enum

/**
 * 配送方法に関するenum
 */
enum class DeliveryMethod(val id: Int, val value: String) {
    NORMAL(1, "通常配送"),
    SPECIFY(2, "日時指定配送")
}