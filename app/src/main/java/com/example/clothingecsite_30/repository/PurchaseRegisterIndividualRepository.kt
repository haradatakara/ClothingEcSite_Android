package com.example.clothingecsite_30.repository

import com.eclipsesource.json.Json
import com.example.clothingecsite_30.model.Address
import com.example.clothingecsite_30.util.http.Http
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 *
 */
class PurchaseRegisterIndividualRepository {

    suspend fun fetchAddress(zipCode: Int): Address? {
        val url = "http://zipcloud.ibsnet.co.jp/api/search?zipcode=${zipCode}"
        var result: Address? = null
        val http = Http()
        return withContext(Dispatchers.IO) {
            async(Dispatchers.IO) {
                http.httpGet(url)
            }.await().let {
                val address = Json.parse(it).asObject()
                if(!address.get("results").isNull) {
                    result = Address(
                        zipCode,
                        address.get("results").asArray()[0].asObject().get("address1").asString(),
                        address.get("results").asArray()[0].asObject().get("address2").asString(),
                        address.get("results").asArray()[0].asObject().get("address3").asString(),
                    )
                }
            }
            return@withContext result
        }
    }
}