package com.cecbrain.core_framework.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cecbrain.core_framework.network.model.BaseResponse
import com.cecbrain.core_framework.util.toBaseException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 框架核心 BaseViewModel
 */
abstract class BaseViewModel<S : IUiState, I : IUiIntent, E : IUiEffect>(initialState: S) : ViewModel() {

    // 1. UI 业务状态
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    // 2. 一次性副作用 (Toast, Navigation等)
    private val _uiEffect = MutableSharedFlow<E>()
    val uiEffect = _uiEffect.asSharedFlow()

    // 3. 页面大状态控制 (CONTENT, EMPTY, ERROR, LOADING)
    private val _pageStatus = MutableStateFlow(PageStatus.CONTENT)
    val pageStatus = _pageStatus.asStateFlow()

    // 4. 最近一次发生的异常（用于 ErrorView 展示）
    private val _lastError = MutableStateFlow<BaseException?>(null)
    val lastError = _lastError.asStateFlow()

    /**
     * 子类必须实现：处理用户意图
     */
    abstract fun handleIntent(intent: I)

    fun sendIntent(intent: I) = handleIntent(intent)

    protected fun setState(reduce: S.() -> S) {
        _uiState.value = _uiState.value.reduce()
    }

    protected fun setEffect(effect: E) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }

    protected fun updatePageStatus(status: PageStatus) {
        _pageStatus.value = status
    }

    /**
     * 基础协程启动：具备异常捕获能力
     */
    protected fun safeLaunch(
        onError: ((BaseException) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val handler = CoroutineExceptionHandler { _, throwable ->
            val exception = throwable.toBaseException()
            _lastError.value = exception
            viewModelScope.launch {
                onError?.invoke(exception) ?: handleDefaultError(exception)
            }
        }
        return viewModelScope.launch(handler) { block() }
    }

    /**
     * 核心网络请求方法：实现 PageStatus 自动化联动
     * @param block 挂起函数请求体
     * @param isFirstLaunch 是否是初始化加载（决定是否切换全屏状态页）
     * @param onSuccess 成功回调
     */
    protected fun <T> request(
        block: suspend () -> BaseResponse<T>,
        isFirstLaunch: Boolean = false,
        onSuccess: (T?) -> Unit
    ) {
        safeLaunch(
            onError = { error ->
                if (isFirstLaunch) {
                    updatePageStatus(PageStatus.ERROR)
                } else {
                    handleDefaultError(error)
                }
            }
        ) {
            if (isFirstLaunch) updatePageStatus(PageStatus.LOADING)

            val response = block()

            if (response.isSuccess) {
                val data = response.data
                if (isFirstLaunch) {
                    val isEmpty = data == null || (data is List<*> && data.isEmpty())
                    updatePageStatus(if (isEmpty) PageStatus.EMPTY else PageStatus.CONTENT)
                }
                onSuccess(data)
            } else {
                throw Exception(response.msg) // 触发 safeLaunch 的异常拦截
            }
        }
    }

    /**
     * 默认错误处理，子类可重写
     */
    open fun handleDefaultError(error: BaseException) {
        // 比如：这里可以发送一个通用的 Toast Effect
    }
}