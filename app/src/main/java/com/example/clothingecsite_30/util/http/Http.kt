package com.example.clothingecsite_30.util.http
import okhttp3.OkHttpClient
import okhttp3.Request

class Http {
    fun httpGet(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()
        val response = HttpClient.instance.newCall(request).execute()
        val body = response.body?.string()
        return body
    }

    object HttpClient {
        val instance = OkHttpClient()
    }
}
