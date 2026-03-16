package com.cecbrain.core_framework.network.interceptors

interface IHeaderProvider {
    fun getHeaders(): Map<String, String>
}
