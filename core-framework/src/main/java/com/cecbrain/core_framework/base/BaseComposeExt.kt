package com.cecbrain.core_framework.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * 在 Compose 中快速获取状态的便捷方法
 */
@Composable
fun <S : IUiState, I : IUiIntent, E : IUiEffect> BaseViewModel<S, I, E>.collectState() =
    this.uiState.collectAsStateWithLifecycle()

@Composable
fun <S : IUiState> kotlinx.coroutines.flow.StateFlow<S>.collectAsState(): State<S> =
    this.collectAsStateWithLifecycle()

@Composable
fun <E : IUiEffect> CollectEffect(
    effectFlow: Flow<E>,
    onEffect: (E) -> Unit
) {
    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            onEffect(effect)
        }
    }
}