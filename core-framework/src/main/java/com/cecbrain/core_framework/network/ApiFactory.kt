package com.cecbrain.core_framework.network;

import com.cecbrain.core_framework.network.interceptors.HeaderInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(HeaderInterceptor())
            .build()
    }

    private var retrofit: Retrofit? = null

    /**
     * 初始化方法，通常在 Application 中调用
     */
    fun init(baseUrl: String) {
        val contentType = "application/json".toMediaType()
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit?.create(service)
            ?: throw RuntimeException("ApiFactory 必须先调用 init() 初始化")
    }
}
