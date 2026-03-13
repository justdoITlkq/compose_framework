[![](https://jitpack.io/v/justdoITlkq/compose_framework.svg)](https://jitpack.io/#justdoITlkq/compose_framework)
# Compose MVI 开发框架

本项目提供了一套基于 Jetpack Compose 的 MVI 架构开发模板，通过 BaseViewModel 与 BaseScreen 的联动，实现
UI 状态自动管理。

---

## 🛠️ 使用指南

### 第一步：定义契约 (Contract)

在单一文件中定义页面的 State、Intent 和 Effect。

```kotlin
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
}@HiltViewModel
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
```

### 第二步：实现逻辑层 (ViewModel)

继承 BaseViewModel，使用内置的 request 方法发起网络请求。

```kotlin
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
```

### 第三步：构建 UI 层 (Compose Screen)

使用 BaseScreen 装饰你的布局。

```kotlin
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
```

---

## 📦 如何引入框架

在你的项目 build.gradle 中添加依赖：

```kotlin
dependencies {
    implementation("com.github.justdoITlkq/compose_framework:0.0.2-SNAPSHOT")
}
```

## 💡 核心组件说明

BaseViewModel: 提供 request 方法，自动分发网络错误。

BaseScreen: 顶层容器，内置了全屏加载、错误重试逻辑。

IUiState: 标记接口，所有页面的 State 类必须实现。