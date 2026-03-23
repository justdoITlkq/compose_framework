package com.cecbrain.common.ext

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 将毫秒时间戳转换为默认格式的字符串 (2026-03-19 18:30:00)
 */
fun Long.toDateTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val instant = Instant.ofEpochMilli(this)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return dateTime.format(DateTimeFormatter.ofPattern(pattern))
}

/**
 * 判断两个时间戳是否为同一天
 */
fun Long.isSameDay(other: Long): Boolean {
    val date1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault()).toLocalDate()
    val date2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(other), ZoneId.systemDefault()).toLocalDate()
    return date1 == date2
}

/**
 * 转换成“刚刚”、“n分钟前”、“n小时前”或具体日期
 * 适用于社交类、资讯类 App 的时间展示
 */
fun Long.toFriendlyTimeSpan(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
        diff < 60 * 1000 -> "刚刚"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
        else -> this.toDateTimeString("yyyy-MM-dd")
    }
}