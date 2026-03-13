package com.cecbrain.core_framework.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 框架核心 BaseViewModel
 */
abstract class BaseViewModel<S : IUiState, I : IUiIntent, E : IUiEffect>(
    initialState: S
) : ViewModel() {

    private val tag = this.javaClass.simpleName

    // 1. 核心数据流
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<E>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    // 2. 简单的防抖控制 (针对重复的 Intent)
    private var lastIntent: I? = null
    private var lastIntentTime = 0L

    abstract fun handleIntent(intent: I)

    /**
     * 发送意图：支持可选防抖
     */
    fun sendIntent(intent: I, debounce: Long = 0L) {
        val currentTime = System.currentTimeMillis()
        if (debounce > 0 && intent == lastIntent && (currentTime - lastIntentTime < debounce)) {
            return // 拦截频繁重复操作
        }

        lastIntent = intent
        lastIntentTime = currentTime

        // 生成这一组操作的唯一标识，方便 ELK 链路追踪
        val flowId = UUID.randomUUID().toString().take(8)

        trace(flowId, "ACTION", "Receive Intent: $intent")

        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    /**
     * 更新状态：对比新旧状态，仅在变化时打日志
     */
    protected fun setState(reduce: S.() -> S) {
        _uiState.update { oldState ->
            val newState = oldState.reduce()
            if (newState != oldState) {
                // 这里可以用 flowId 关联，但内部更新通常直接记录
                trace(null, "STATE", "New State: $newState")
            }
            newState
        }
    }

    /**
     * 发送副作用
     */
    protected fun setEffect(builder: () -> E) {
        val effect = builder()
        trace(null, "EFFECT", "Send Effect: $effect")
        viewModelScope.launch { _uiEffect.send(effect) }
    }

    /**
     * ELK 友好的日志格式化
     */
    private fun trace(flowId: String?, type: String, message: String) {
        // 格式化为：[VMName][Type][FlowID] Message
        // 这种结构化日志后续通过 Logstash 的 Grok 插件非常好解析
        val idPart = if (flowId != null) "[$flowId]" else ""
        val logContent = "[$tag][$type]$idPart $message"

        // 1. 本地控制台输出
        Log.d("MVI_TRACE", logContent)

        // 2. 预留 ELK 上传入口
        // if (type == "STATE" || type == "ACTION") {
        //    upload(mapOf(
        //        "level" to "DEBUG",
        //        "tag" to tag,
        //        "type" to type,
        //        "flow_id" to flowId,
        //        "content" to message
        //    ))
        // }
    }
}