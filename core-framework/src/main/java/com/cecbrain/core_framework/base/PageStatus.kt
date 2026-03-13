package com.cecbrain.core_framework.base

sealed interface PageStatus {
    data object Loading : PageStatus
    data object Empty : PageStatus
    data object Success : PageStatus
    data class Error(val msg: String) : PageStatus
}