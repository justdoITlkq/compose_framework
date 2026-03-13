package com.cecbrain.compose_framework

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cecbrain.core_framework.base.CollectEffect
import com.cecbrain.core_framework.base.PageStatus
import com.cecbrain.core_framework.base.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    // 页面进入时触发加载
    LaunchedEffect(Unit) {
        viewModel.sendIntent(TodoIntent.FetchTodoList)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("我的待办") }) }
    ) { padding ->
        // 根据 pageStatus 渲染不同的内容
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            when (val status = state.pageStatus) {
                is PageStatus.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is PageStatus.Empty -> {
                    Text("暂无数据，点击屏幕重试", Modifier.align(Alignment.Center))
                }
                is PageStatus.Error -> {
                    Column(Modifier.align(Alignment.Center)) {
                        Text("出错啦: ${status.msg}")
                        Button(onClick = { viewModel.sendIntent(TodoIntent.FetchTodoList) }) {
                            Text("重试")
                        }
                    }
                }
                is PageStatus.Success -> {
                    // 真正的复杂业务布局
                    TodoContent( viewModel)
                }
            }
        }
    }
}

@Composable
fun TodoContent(viewModel: TodoViewModel) {
    val state by viewModel.uiState.collectAsState()

    // 监听 Effect (比如添加成功的提示)
    CollectEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is TodoEffect.ShowToast -> { /* 执行原生 Toast */ }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 输入区域
        Row {
            TextField(
                value = state.inputText,
                onValueChange = { viewModel.sendIntent(TodoIntent.UpdateInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("输入待办事项...") }
            )
            Button(onClick = { viewModel.sendIntent(TodoIntent.AddTodo) }) {
                Text("添加")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 列表区域
        LazyColumn {
            items(state.todoList.size) { index ->
                TodoRow(
                    item = state.todoList[index],
                    onToggle = { viewModel.sendIntent(TodoIntent.ToggleTodo(state.todoList[index].id)) },
                    onDelete = { viewModel.sendIntent(TodoIntent.DeleteTodo(state.todoList[index].id)) }
                )
            }
        }
    }
}

@Composable
fun TodoRow(item: TodoItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = item.isDone, onCheckedChange = { onToggle() })
        Text(
            text = item.text,
            modifier = Modifier.weight(1f),
            style = if (item.isDone) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle.Default
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = null)
        }
    }
}