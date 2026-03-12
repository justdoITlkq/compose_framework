package com.cecbrain.compose_framework

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cecbrain.core_framework.component.BaseScreen

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                is LoginEffect.NavigateToHome -> { /* 跳转逻辑 */ }
            }
        }
    }

    BaseScreen(
        viewModel = viewModel,
        onRetry = { viewModel.sendIntent(LoginIntent.ReFetchConfig) }
    ) {
        // 这里是 PageStatus.CONTENT 状态下的真正布局
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp), verticalArrangement = Arrangement.Center) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = it.username,
                onValueChange = { viewModel.sendIntent(LoginIntent.InputName(it)) },
                label = { Text("用户名") }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.sendIntent(LoginIntent.ClickLogin) },
                enabled = !it.isLoading
            ) {
                if (it.isLoading) CircularProgressIndicator(Modifier.size(20.dp))
                else Text("登录")
            }
        }
    }
}
