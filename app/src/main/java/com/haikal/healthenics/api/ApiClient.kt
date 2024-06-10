package com.haikal.healthenics.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import com.haikal.healthenics.BuildConfig
import com.haikal.healthenics.utils.TokenGenerator
import okhttp3.ConnectionPool
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    private val okhttp = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)
            }
            addInterceptor(AuthorizationInterceptor()) // Tambahkan interceptor untuk Authorization
            addInterceptor(GzipRequestInterceptor()) // Tambahkan interceptor untuk Gzip
            connectionPool(ConnectionPool(5, 30, TimeUnit.SECONDS))
        }
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okhttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

class GzipRequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val compressedRequest = originalRequest.newBuilder()
            .header("Content-Encoding", "gzip")
            .method(originalRequest.method, originalRequest.body)
            .build()
        return chain.proceed(compressedRequest)
    }
}

class AuthorizationInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${TokenGenerator.TOKEN}")
            .build()
        return chain.proceed(newRequest)
    }
}