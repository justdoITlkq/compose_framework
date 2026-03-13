# Compose MVI 开发框架

本项目提供了一套基于 Jetpack Compose 的 MVI 架构开发模板，通过 BaseViewModel 与 BaseScreen 的联动，实现
UI 状态自动管理。

---

## 🛠️ 使用指南

### 第一步：定义契约 (Contract)

在单一文件中定义页面的 State、Intent 和 Effect。

```kotlin
data class LoginState(val username: String = "") : IUiState

sealed class LoginIntent : IUiIntent {
    data class InputName(val name: String) : LoginIntent()
    data object ClickLogin : LoginIntent()
}

sealed class LoginEffect : IUiEffect {
    data class ShowToast(val message: String) : LoginEffect()
}
```

### 第二步：实现逻辑层 (ViewModel)

继承 BaseViewModel，使用内置的 request 方法发起网络请求。

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor() :
    BaseViewModel<LoginState, LoginIntent, LoginEffect>(LoginState()) {

    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.InputName -> setState { copy(username = intent.name) }
            is LoginIntent.ClickLogin -> {
                // do login and set effect
                setEffect(LoginEffect.ShowToast("登录成功"))
            }
        }
    }
    
    

}
```

### 第三步：构建 UI 层 (Compose Screen)

使用 BaseScreen 装饰你的布局。

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    BaseScreen(
        viewModel = viewModel,
        onRetry = { viewModel.sendIntent(LoginIntent.ClickLogin) }
    ) { 
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            TextField(
                value = it.username,
                onValueChange = { viewModel.sendIntent(LoginIntent.InputName(it)) },
                label = { Text("用户名") }
            )
            Button(onClick = { viewModel.sendIntent(LoginIntent.ClickLogin) }) {
                Text("登录")
            }
            
            Button(onClick = { viewModel.sendIntent(LoginIntent.ClickLogin) }) {
                Text("其他意图")
            }
        }
    }
}
```

---

## 📦 如何引入框架

在你的项目 build.gradle 中添加依赖：

```kotlin
dependencies {
    implementation("com.github.YourName:compose-framework:1.0.0")
}
```

## 💡 核心组件说明

BaseViewModel: 提供 request 方法，自动分发网络错误。

BaseScreen: 顶层容器，内置了全屏加载、错误重试逻辑。

IUiState: 标记接口，所有页面的 State 类必须实现。