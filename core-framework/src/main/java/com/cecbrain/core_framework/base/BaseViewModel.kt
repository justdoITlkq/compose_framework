package com.cecbrain.core_framework.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

abstract class BaseViewModel<S : IUiState, I : IUiIntent, E : IUiEffect>(
    initialState: S
) : ViewModel() {

    private val tag = "MVI_${this.javaClass.simpleName}"

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<E>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private var lastIntent: I? = null
    private var lastIntentTime = 0L
    private val currentFlowId = ThreadLocal<String>()

    /**
     * 全自动异常捕获并弹出错误提示
     */
    protected open val commonExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val id = currentFlowId.get() ?: "UNKNOWN"
        val errorMessage = throwable.localizedMessage ?: "未知错误"

        trace("ERROR", "❌ Crash caught: $errorMessage", id)

        // 自动触发一个通用的错误 Effect (由子类决定 E 是否包含 CommonEffect)
        handleGlobalError(throwable)
    }

    /**
     * 子类可以重写此方法来实现自定义的错误逻辑
     * 默认行为是打印日志，你可以通过 setEffect 弹出 Toast
     */
    protected open fun handleGlobalError(throwable: Throwable) {
        // 这里留空或提供默认实现
        // 业务层如果想弹 Toast，可以在子类里重写并调用 setEffect
    }

    abstract fun handleIntent(intent: I)

    fun sendIntent(intent: I, debounceMs: Long = 0L) {
        val flowId = UUID.randomUUID().toString().take(8)

        if (isDebounced(intent, debounceMs)) {
            trace("IGNORE", "➔ Intent Debounced: $intent", flowId)
            return
        }

        lastIntent = intent
        lastIntentTime = System.currentTimeMillis()

        trace("ACTION", "➔ Input: $intent", flowId)

        viewModelScope.launch(commonExceptionHandler) {
            currentFlowId.set(flowId)
            try {
                handleIntent(intent)
            } finally {
                currentFlowId.remove()
            }
        }
    }

    protected fun setState(reduce: S.() -> S) {
        _uiState.update { oldState ->
            val newState = oldState.reduce()
            if (newState != oldState) {
                val id = currentFlowId.get() ?: "ASYNC"
                trace("STATE", "Δ Change: $oldState ➔ $newState", id)
            }
            newState
        }
    }

    protected fun setEffect(builder: () -> E) {
        val effect = builder()
        val id = currentFlowId.get() ?: "ASYNC"
        trace("EFFECT", "⚡ Output: $effect", id)
        viewModelScope.launch(commonExceptionHandler) {
            _uiEffect.send(effect)
        }
    }

    private fun trace(type: String, message: String, flowId: String?) {
        val idPart = flowId?.let { "[$it]" } ?: ""
        Log.d(tag, "[$type]$idPart $message")
    }

    private fun isDebounced(intent: I, debounceMs: Long): Boolean {
        if (debounceMs <= 0) return false
        return (intent == lastIntent) && (System.currentTimeMillis() - lastIntentTime < debounceMs)
    }
}