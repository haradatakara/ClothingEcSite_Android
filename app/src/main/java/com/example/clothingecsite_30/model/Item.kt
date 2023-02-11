package com.example.clothingecsite_30.model

import com.google.firebase.firestore.DocumentId

/**
 * 商品詳細に関してまとめるモデルクラス
 */
data class Item(
    @DocumentId val documentId: String = "",
    val itemId: Int = 0,
    val name: String = "name",
    val price: Int = 0,
    val genre: String = "genre",
    val imgPath: String = "imgPath"
)