package com.cecbrain.compose_framework

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cecbrain.compose_framework.ui.theme.Compose_frameworkTheme
import com.cecbrain.core_framework.network.ConnectivityNetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val networkMonitor = ConnectivityNetworkMonitor(applicationContext)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                networkMonitor.isOnline.collect { isOnline ->
                    // 打印日志确认是否收到信号
                    Log.d("NetworkTest", "Status: $isOnline")

                    Toast.makeText(
                        this@MainActivity,
                        if (isOnline) "网络已连接" else "网络已断开",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        setContent {
            Scaffold { padding ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)) {
                    TodoScreen()
                }
            }
        }
    }
}
