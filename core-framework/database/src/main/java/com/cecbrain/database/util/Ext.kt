package com.cecbrain.database.util

import com.cecbrain.common.model.AppResult
import kotlinx.coroutines.flow.*

// 三级缓存
inline fun <ResultType,RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    emit(AppResult.Loading)
    val data = query().first() // 拿到第一手本地数据

    val flow = if (shouldFetch(data)) {
        try {
            saveFetchResult(fetch()) // 联网更新并保存
            query().map { AppResult.Success(it) } // 重新从本地流出新数据
        } catch (throwable: Throwable) {
            query().map { AppResult.Success(it) } // 联网失败，依然展示本地旧数据
        }
    } else {
        query().map { AppResult.Success(it) } // 直接用本地
    }

    emitAll(flow)
}