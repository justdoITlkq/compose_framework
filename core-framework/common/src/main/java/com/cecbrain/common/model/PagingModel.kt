package com.cecbrain.common.model

data class PagingModel<T>(
    val items: List<T>,
    val page: Int,
    val total: Int,
    val hasMore: Boolean
)