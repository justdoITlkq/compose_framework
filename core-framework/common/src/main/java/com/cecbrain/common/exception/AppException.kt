package com.cecbrain.common.exception

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppException(
    val code: Int = -1,
    val msg: String,
    cause: Throwable? = null
) :Exception(msg, cause){
    companion object {
        /**
         * 供 BaseRepository 调用的映射工具
         */
        fun map(e: Throwable): AppException {
            return when (e) {
                is AppException -> e
                is UnknownHostException, is ConnectException -> NetworkException("无法连接到服务器", e)
                is SocketTimeoutException -> NetworkException("连接超时，请重试", e)
                // 401 错误通常由 Interceptor 抛出或在 BaseRepository 判断 code 后手动转为 AuthError
                else -> UnknownException(e)
            }
        }
    }

    class NetworkException(
        msg: String = "",
        cause: Throwable? = null
    ) : AppException(-100, msg, cause)

    class BusinessException(
        code: Int,
        msg: String = "",
    ) : AppException(code, msg)

    class AuthError(message: String = "请重新登录")
        : AppException(401, message)

    class UnknownException(
        cause: Throwable? = null
    ) : AppException(-1, "未知错误", cause)
}