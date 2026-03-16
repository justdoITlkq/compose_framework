package com.cecbrain.core_framework.base;

open class CommonEffect : IUiEffect {
    data class ShowToast(val message: String) : CommonEffect()
    data class ShowErrorDialog(val title: String, val message: String) : CommonEffect()
}
