package com.haikal.healthenics.utils

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import com.google.gson.JsonParser
import java.io.IOException

class TokenGenerator {
    companion object {
        val TOKEN: String by lazy {
            executeTokenCommand()
        }

        private fun executeTokenCommand(): String {
            val client = OkHttpClient()
            val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val body = RequestBody.create(mediaType, "grant_type=client_credentials&scope=basic")
            val credentials = Credentials.basic("30b2c6211d0b49efa65151ffdc9c6a90", "9371ed8db0464638882382e16a477248")
            val request = Request.Builder()
                .url("https://oauth.fatsecret.com/connect/token")
                .post(body)
                .header("Authorization", credentials)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseBody = response.body?.string()  // Updated from response.body()?.string()
                val jsonObject = JsonParser.parseString(responseBody).asJsonObject
                return jsonObject.get("access_token").asString
            }
        }
    }
}