package com.cecbrain.core_framework.base

data class BaseException(
    val code: Int = -1,
    val msg: String = "未知错误",
    val type: ErrorType = ErrorType.UNKNOWN,
    val throwable: Throwable? = null
)