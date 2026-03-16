package com.cecbrain.core_framework.network;

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

    // OkHttp默认配置
    @JvmStatic
    fun getBaseOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
    }

    private var internalClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    /**
     * 初始化方法，通常在 Application 中调用
     */
    fun init(baseUrl: String, client: OkHttpClient? = null) {
        val contentType = "application/json".toMediaType()
        // 如果用户没传，就用默认的基础 client
        internalClient = client ?: getBaseOkHttpClientBuilder().build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(internalClient!!)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
        retrofit
    }

    fun <T> create(service: Class<T>): T {
        return retrofit?.create(service)
            ?: throw RuntimeException("ApiFactory 必须先调用 init() 初始化")
    }

    fun getClient(): OkHttpClient {
        return internalClient
            ?: throw RuntimeException("ApiFactory 必须先调用 init() 初始化")
    }
}
