package com.cecbrain.compose_framework.di

import com.cecbrain.network.di.BaseUrl
import com.cecbrain.network.di.NetworkInterceptors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = "https://api.cecbrain.com/" // 你的真实 API 地址

    @Provides
    @NetworkInterceptors
    @JvmSuppressWildcards
    fun provideInterceptors(): List<Interceptor> = emptyList() // 暂时为空，后续可加 AuthInterceptor
}