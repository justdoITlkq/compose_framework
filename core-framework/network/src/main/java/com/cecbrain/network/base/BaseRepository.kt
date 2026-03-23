package com.cecbrain.network.base

import com.cecbrain.common.exception.AppException
import com.cecbrain.common.model.AppResult
import com.cecbrain.network.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {

    suspend fun <T> executeRequest(
        block: suspend () -> ApiResponse<T>
    ): AppResult<T> = withContext(Dispatchers.IO) {
        try {
            val response = block()
            if (response.isSuccess()) {
                if (response.data == null) {
                    return@withContext AppResult.Error(exception = AppException.BusinessException(-1, "数据为空"))
                } else {
                    return@withContext AppResult.Success(response.data)
                }
            } else {
                val exception = when (response.code) {
                    401 -> AppException.AuthError(response.msg)
                    // 所有的业务逻辑错误都归类为 BusinessException
                    else -> AppException.BusinessException(response.code, response.msg)
                }
                return@withContext AppResult.Error(exception)
            }
        } catch (e: Exception) {
            return@withContext AppResult.Error(AppException.map(e))
        }
    }
}