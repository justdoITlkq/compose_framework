package com.cecbrain.compose_framework

import androidx.lifecycle.viewModelScope
import com.cecbrain.core_framework.base.BaseViewModel
import com.cecbrain.core_framework.base.PageStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class TodoViewModel @Inject constructor() : BaseViewModel<TodoState, TodoIntent, TodoEffect>(TodoState()) {


    override fun handleIntent(intent: TodoIntent) {

        when (intent) {
            is TodoIntent.UpdateInput -> {
                setState {
                    copy(inputText = intent.text)
                }
            }

            is TodoIntent.AddTodo -> {
                if (uiState.value.inputText.isBlank()){
                    setEffect { TodoEffect.ShowToast("请输入待办内容！") }
                    return
                }
                val newItem = TodoItem(text = uiState.value.inputText, isDone = false)
                setState {
                    copy(
                        todoList = todoList + newItem,
                        inputText = ""
                    )
                }
                setEffect { TodoEffect.ShowToast("添加成功！") }
            }

            is TodoIntent.ToggleTodo -> {
                setState {
                    val newList = todoList.map {
                        if (it.id == intent.id) it.copy(isDone = !it.isDone) else it
                    }
                    copy(todoList = newList)
                }
            }

            is TodoIntent.DeleteTodo -> {
                setState {
                    copy(todoList = todoList.filter { it.id != intent.id })
                }
            }

            TodoIntent.FetchTodoList -> {
                fetchTodoList()
            }
        }
    }

    /**
     * mock fetch todo list data by delay
     */
    private fun fetchTodoList() {
        setState {
            copy(pageStatus = PageStatus.Loading)
        }
        viewModelScope.launch {
            delay(2000)

            val a = Random.nextInt(10)
            // 模拟成功
            if (a > 3) {
                setState {
                    copy(
                        todoList = listOf(
                            TodoItem(text = "Todo 1", isDone = false),
                            TodoItem(text = "Todo 2", isDone = true),
                            TodoItem(text = "Todo 3", isDone = false),
                        ),
                        pageStatus = PageStatus.Success
                    )
                }
            }else {
                setState {
                    copy(
                        pageStatus = PageStatus.Error("请求失败")
                    )
                }
            }

        }

    }
}