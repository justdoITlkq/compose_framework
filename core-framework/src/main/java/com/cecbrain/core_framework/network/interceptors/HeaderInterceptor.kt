package com.cecbrain.core_framework.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
        // 可以在这里统一动态注入 Token
        // .header("Authorization", "Bearer ${TokenManager.getToken()}")

        return chain.proceed(requestBuilder.build())
    }
}