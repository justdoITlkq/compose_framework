package com.cecbrain.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryGreen,
    error = ErrorRed,
    background = BackgroundGray
)

@Composable
fun BaseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        // 这里后续可以加入自定义的 Typography 和 Shapes
        content = content
    )
}