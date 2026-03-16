package com.cecbrain.core_framework.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val provider: IHeaderProvider? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("X-Platform", "android")

        provider?.getHeaders()?.forEach {
            requestBuilder.header(it.key, it.value)
        }
        return chain.proceed(requestBuilder.build())
    }
}