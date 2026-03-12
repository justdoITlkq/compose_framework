package com.cecbrain.core_framework.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(val code: Int, val msg: String, val data:T) {
    val isSuccess: Boolean get() = code == 200
}