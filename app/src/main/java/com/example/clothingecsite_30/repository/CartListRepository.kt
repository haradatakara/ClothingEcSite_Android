package com.example.clothingecsite_30.repository

import com.example.clothingecsite_30.model.Cart
import com.example.clothingecsite_30.model.Item
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.ArrayList

class CartListRepository {

    private val fireDb = Firebase.firestore
    private var insertDataBase = false

    suspend fun onClickDeleteItem(cart: Cart?): Boolean {
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


    suspend fun fetchCartItems(): ArrayList<MutableMap<String, *>> {
        return withContext(Dispatchers.IO) {
            var arrayItems: ArrayList<MutableMap<String, *>> = ArrayList()
            val cart = fireDb
                .collection("cart")
                .document(Firebase.auth.uid!!)
                .get().await()
            if (cart != null) {
                arrayItems = cart.get("item") as ArrayList<MutableMap<String, *>>
            }

            return@withContext arrayItems
        }
    }

    suspend fun fetchItemInfo(arrayItems: ArrayList<MutableMap<String, *>>): MutableList<Cart> {
        println(arrayItems)
        return withContext(Dispatchers.IO) {
            val itemList: MutableList<Cart> = mutableListOf()
            arrayItems.forEach { item ->
                val fetchItem = fireDb.collection("items")
                    .whereEqualTo("itemId", item["itemId"])
                    .get().await()
                fetchItem.forEach { itemInfo ->
                    itemList.add(
                        Cart(
                            item["itemId"] as Long,
                            itemInfo["name"] as String,
                            item["totalPrice"] as Long,
                            itemInfo["imgPath"] as String,
                            item["addCartAt"].toString()
                        )
                    )
                }
        }
        println("itemList:$itemList")
        return@withContext itemList
    }
}

}