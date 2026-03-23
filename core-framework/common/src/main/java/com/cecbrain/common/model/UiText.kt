package com.cecbrain.common.model

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(
        val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText()
}