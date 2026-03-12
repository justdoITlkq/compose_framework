package com.cecbrain.compose_framework

import com.cecbrain.core_framework.base.IUiEffect
import com.cecbrain.core_framework.base.IUiIntent
import com.cecbrain.core_framework.base.IUiState

// UI 状态
data class LoginState(
    val username: String = "",
    val isLoading: Boolean = false // 用于按钮上的局部加载状态
) : IUiState

// 用户操作
sealed class LoginIntent : IUiIntent {
    data class InputName(val name: String) : LoginIntent()
    object ClickLogin : LoginIntent()
    object ReFetchConfig : LoginIntent() // 测试重试逻辑
}

// 一次性副作用
sealed class LoginEffect : IUiEffect {
    data class ShowToast(val message: String) : LoginEffect()
    object NavigateToHome : LoginEffect()
}