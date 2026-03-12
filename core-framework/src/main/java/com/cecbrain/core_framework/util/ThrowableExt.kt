package com.cecbrain.core_framework.util

import com.cecbrain.core_framework.base.BaseException
import com.cecbrain.core_framework.base.ErrorType

fun Throwable.toBaseException(): BaseException {
    return when (this) {
            is java.net.SocketTimeoutException -> BaseException(-2, "连接超时", ErrorType.TIMEOUT)
        is java.net.ConnectException, is java.net.UnknownHostException -> BaseException(-3, "无网络连接", ErrorType.NETWORK)
        is retrofit2.HttpException -> {
            when (this.code()) {
                401 -> BaseException(401, "登录失效", ErrorType.UNAUTHORIZED)
                else -> BaseException(this.code(), "服务器异常", ErrorType.SERVER)
            }
        }
        else -> BaseException(-1, this.message ?: "未知错误", ErrorType.UNKNOWN)
    }
}