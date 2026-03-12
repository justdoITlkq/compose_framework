package com.cecbrain.core_framework.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cecbrain.core_framework.base.BaseViewModel
import com.cecbrain.core_framework.base.IUiEffect
import com.cecbrain.core_framework.base.IUiIntent
import com.cecbrain.core_framework.base.IUiState
import com.cecbrain.core_framework.base.PageStatus
import com.cecbrain.core_framework.base.collectState

@Composable
fun <S : IUiState, I : IUiIntent, E : IUiEffect> BaseScreen(
    viewModel: BaseViewModel<S, I, E>,
    onRetry: () -> Unit = {},
    loadingContent: @Composable () -> Unit = { DefaultLoadingView() },
    content: @Composable (S) -> Unit
) {
    val state by viewModel.collectState()
    val pageStatus by viewModel.pageStatus.collectAsStateWithLifecycle()

    when (pageStatus) {
        PageStatus.LOADING -> loadingContent() // 这里根据传入决定显示什么
        PageStatus.EMPTY -> EmptyView()
        PageStatus.ERROR -> ErrorView(null, onRetry) // 传入 error 信息可自行扩展
        else -> content(state) // CONTENT 状态和常规加载状态都走这里
    }
}