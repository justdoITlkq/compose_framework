package com.cecbrain.compose_framework

import com.cecbrain.core_framework.base.BaseException
import com.cecbrain.core_framework.base.BaseViewModel
import com.cecbrain.core_framework.network.model.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor() :
    BaseViewModel<LoginState, LoginIntent, LoginEffect>(LoginState()) {


    init {
        // 模拟页面初始化获取配置
        fetchInitConfig()
    }
    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.InputName -> setState { copy(username = intent.name) }
            is LoginIntent.ClickLogin -> doLogin()
            is LoginIntent.ReFetchConfig -> fetchInitConfig()
        }
    }
    private fun fetchInitConfig() {
        // 使用 isFirstLaunch = true，测试全屏 LOADING -> CONTENT/ERROR/EMPTY
        request(
            block = { login("") }, // 模拟请求
            isFirstLaunch = true
        ) {
            setEffect(LoginEffect.NavigateToHome)
        }
    }

    private fun doLogin() {
        setState { copy(isLoading = true) }
        // 普通请求，不破坏页面现有内容，仅通过 state 控制按钮菊花
        request(block = { login(uiState.value.username) }) {
            setState { copy(isLoading = false) }
            setEffect(LoginEffect.ShowToast("登录成功"))
        }
    }

    private suspend fun login(username: String): BaseResponse<Int> {
        delay(3000)
        return BaseResponse(code = 200, msg = "登录成功", data = 1)
    }

    override fun handleDefaultError(error: BaseException) {
        // 如果不是全屏错误，就弹个 Toast
        setEffect(LoginEffect.ShowToast(error.msg))
    }
}