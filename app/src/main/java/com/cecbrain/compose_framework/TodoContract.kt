package com.cecbrain.compose_framework

import com.cecbrain.core_framework.base.IUiEffect
import com.cecbrain.core_framework.base.IUiIntent
import com.cecbrain.core_framework.base.IUiState
import com.cecbrain.core_framework.base.PageStatus
import java.util.UUID

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isDone: Boolean
)

data class TodoState(
    val todoList: List<TodoItem> = emptyList(),
    val inputText: String = "",
    val pageStatus: PageStatus = PageStatus.Loading
) : IUiState


sealed class TodoIntent : IUiIntent {
    data class UpdateInput(val text: String) : TodoIntent() // 输入文字
    data object AddTodo : TodoIntent()                     // 点击添加
    data class ToggleTodo(val id: String) : TodoIntent()   // 勾选/取消
    data class DeleteTodo(val id: String) : TodoIntent()   // 删除
    data object FetchTodoList : TodoIntent() // 联网获取列表
}

sealed class TodoEffect:IUiEffect {
    data class ShowToast(val message: String) : TodoEffect() // 显示提示
}
