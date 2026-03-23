package com.cecbrain.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T? = null
){
    fun isSuccess(): Boolean {
        return code == 200
    }
}
