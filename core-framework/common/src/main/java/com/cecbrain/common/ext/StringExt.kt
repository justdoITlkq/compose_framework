package com.cecbrain.common.ext


/**
 * 判断是否是合法的手机号（中国大陆简易版）
 */
fun String.isPhoneNumber(): Boolean {
    return Regex("^1[3-9]\\d{9}$").matches(this)
}

/**
 * 如果字符串为空，则返回指定的默认值
 */
fun String?.orDefault(default: String = ""): String {
    return if (this.isNullOrBlank()) default else this
}