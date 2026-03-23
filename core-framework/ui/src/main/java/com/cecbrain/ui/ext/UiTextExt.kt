package com.cecbrain.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cecbrain.common.model.UiText

@Composable
fun UiText.asString(): String {
    return when (this) {
        is UiText.DynamicString -> value
        is UiText.StringResource -> stringResource(resId, *args.toTypedArray())
    }
}