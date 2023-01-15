package com.example.clothingecsite_30.repository


import com.example.clothingecsite_30.model.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CartListRepository {

    private val fireDb = Firebase.firestore
    private var insertDataBase = false

    suspend fun onClickDeleteItem(cart: Item?): Boolean {
        return withContext(Dispatchers.IO) {
            val cartItemRef = fireDb.collection("items").document("8XEXmuKBrlP46cwEoEVi")
            cartItemRef.update(
                hashMapOf<String, Any>(
                "itemId" to 8,
                "name" to "Gap",
                "price" to 4500,
                "genre" to "shoes",
                "imgPath" to "tobias_tullius_fg15ldqpwrs_unsplash"
                )
            ).addOnSuccessListener {
                insertDataBase = true
            }
            delay(500)
            return@withContext insertDataBase
        }
    }

//    suspend fun fetchItemList(): List<Item> {
//        val itemList: MutableList<Item> = mutableListOf()
//
//        return withContext(Dispatchers.IO) {
//            fireDb.collection("items").get().addOnSuccessListener { items ->
//                println(items.size())
//                items?.forEach {
//                    val data = it.toObject(Item::class.java)
//                    itemList.add(data)
//                }
//            }.addOnFailureListener { e -> println("失敗:${e}") }
//
//            delay(2000)
//            return@withContext itemList
//        }
//    }

}