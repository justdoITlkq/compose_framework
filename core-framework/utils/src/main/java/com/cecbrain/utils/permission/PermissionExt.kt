package com.cecbrain.utils.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


/**
 * 检查权限
 */
fun Context.hasPermission(permission:String):Boolean{
    return ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED
}


/**
 * 批量检查权限
 */
fun Context.hasPermissions(vararg permissions: String): Boolean {
    return permissions.all { hasPermission(it) }
}