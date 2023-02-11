package com.example.clothingecsite_30.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.clothingecsite_30.model.Cart
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class CartListRepository {

    private val fireDb = Firebase.firestore
    private var insertDataBase = false

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun onClickAddCartBtn(
        cartItem: MutableLiveData<Cart>,
    ): Boolean {
        withContext(Dispatchers.IO) {
            val cartItemRef = fireDb.collection("cart").document(Firebase.auth.uid!!).get().await()
            if(cartItemRef.data == null) {
                fireDb.collection("cart").document(Firebase.auth.uid!!).set(
                    hashMapOf(
                        "item" to listOf(hashMapOf(
                            "itemId" to cartItem.value?.itemId,
                            "totalPrice" to cartItem.value?.totalPrice,
                            "addCartAt" to DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                                .format(LocalDateTime.now())
                        ))
                    )
                )
            } else {
                fireDb.collection("cart").document(Firebase.auth.uid!!).update(
                    "item", FieldValue.arrayUnion(
                        hashMapOf(
                            "itemId" to cartItem.value?.itemId,
                            "totalPrice" to cartItem.value?.totalPrice,
                            "addCartAt" to DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                                .format(LocalDateTime.now())
                        )
                    )
                )
            }
            insertDataBase = true
        }
        return insertDataBase
    }

    suspend fun onClickDeleteItem(cart: Cart?): Boolean {
        withContext(Dispatchers.IO) {
            val cartItemRef =
                fireDb
                    .collection("cart")
                    .document(Firebase.auth.uid!!)
            cartItemRef.update(
                "item", FieldValue.arrayRemove(
                    hashMapOf(
                        "itemId" to cart!!.itemId,
                        "totalPrice" to cart.totalPrice,
                        "addCartAt" to cart.addCartAt
                    )
                )
            ).await()
            insertDataBase = true
        }
        return insertDataBase
    }


    suspend fun fetchCartItems(): ArrayList<MutableMap<String, *>> {
        return withContext(Dispatchers.IO) {
            var arrayItems: ArrayList<MutableMap<String, *>> = ArrayList()
            val cart = fireDb.collection("cart").document(Firebase.auth.uid!!).get().await()

            if (cart.data != null) {
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
                val fetchItem =
                    fireDb.collection("items").whereEqualTo("itemId", item["itemId"]).get().await()
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