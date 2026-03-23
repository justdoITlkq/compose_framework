package com.cecbrain.compose_framework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cecbrain.compose_framework.data.dao.TodoDao
import com.cecbrain.compose_framework.data.entity.TodoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoDao: TodoDao
) : ViewModel() {
    // 使用 StateFlow 将数据库查询结果暴露给 UI 层
    val todos: StateFlow<List<TodoEntity>> = todoDao.getAllTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 界面不可见 5 秒后停止监听，节省资源
            initialValue = emptyList()
        )

    fun addTodo(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            todoDao.insertTodo(TodoEntity(title = title))
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }
}