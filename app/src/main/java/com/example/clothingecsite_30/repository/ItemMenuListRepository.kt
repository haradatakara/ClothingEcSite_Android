package com.example.clothingecsite_30.repository

import com.example.clothingecsite_30.model.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * item情報を扱うレポジトリクラス
 */
class ItemMenuListRepository {
    private val fireDb = Firebase.firestore

    // item一覧取得
    suspend fun fetchItemList(): List<Item> {
        return withContext(Dispatchers.IO) {
            return@withContext fireDb.collection("items").get().await().toObjects(Item::class.java)
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