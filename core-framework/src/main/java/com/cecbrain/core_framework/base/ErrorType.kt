package com.cecbrain.core_framework.base

enum class ErrorType {
    NETWORK,      // 网络连接问题（断网、DNS解析失败）
    TIMEOUT,      // 请求超时
    SERVER,       // 服务器错误 (5xx)
    BUSINESS,     // 业务错误 (接口返回 isSuccess 为 false)
    UNAUTHORIZED, // 未授权/登录失效 (401)
    UNKNOWN       // 其他未知错误
}