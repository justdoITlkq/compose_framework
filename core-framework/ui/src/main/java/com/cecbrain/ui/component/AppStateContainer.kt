package com.cecbrain.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cecbrain.common.model.AppResult

@Composable
fun <T> AppStateContainer(
    result: AppResult<T>,
    onRetry: () -> Unit = {},
    content: @Composable (T) -> Unit
) {
    when (result) {
        is AppResult.Loading -> FullScreenLoading()
        is AppResult.Success -> content(result.data)
        is AppResult.Error -> {

        }
    }
}