package com.example.clothingecsite_30.repository

import com.example.clothingecsite_30.model.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * item情報を扱うレポジトリクラス
 */
class ItemMenuListRepository {
    private val fireDb = Firebase.firestore

    // item一覧取得
    suspend fun fetchItemList(): List<Item> {
        var itemList: List<Item> = listOf()
        return withContext(Dispatchers.IO) {
            try {
                fireDb
                    .collection("items")
                    .get()
                    .addOnCompleteListener {
                        itemList = it.result.toObjects(Item::class.java)
                    }
            } catch (_: Exception) {}
            delay(1000)
            return@withContext itemList
        }
    }


    // アイテム詳細情報取得
    suspend fun fetchItem(docId: String): Item {
        return withContext(Dispatchers.IO) {
            return@withContext fireDb
                .collection("items")
                .document(docId)
                .get().await().toObject(Item::class.java)!!
        }
    }
}