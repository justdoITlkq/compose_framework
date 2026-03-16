package com.cecbrain.core_framework.util


import android.util.Log

object MviLogger {
    private const val TAG = "MVI-Trace"

    fun <I, S> logTransition(viewModelName: String, intent: I, oldState: S, newState: S) {
        val message = """
            ╔════════ [ $viewModelName ] ══════════════════════════
            ║ 🟢 Intent: $intent
            ║ ⚪ Old State: $oldState
            ║ 🔵 New State: $newState
            ╚═══════════════════════════════════════════════════════
        """.trimIndent()
        Log.d(TAG, message)
    }
}