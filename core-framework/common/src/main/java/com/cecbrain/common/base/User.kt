package com.cecbrain.common.base

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String, val name: String)
