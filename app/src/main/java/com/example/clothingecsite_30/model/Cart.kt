package com.example.clothingecsite_30.model

/**
 * カート情報に関してまとめるモデルクラス
 */
class Cart(
    itemId: Long,
    name: String,
    price: Long,
    imgPath: String,
    addCartAt: String
) : java.io.Serializable {
    val itemId: Long = itemId
    val name: String = name
    val totalPrice: Long = price
    val imgPath: String = imgPath
    val addCartAt: String = addCartAt
}