package com.cecbrain.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

internal class BigDecimalAdapter {
    @ToJson
    fun toJson(bigDecimal: BigDecimal): String {
        return bigDecimal.toPlainString()
    }

    @FromJson
    fun fromJson(bigDecimal: String): BigDecimal {
        return BigDecimal(bigDecimal)
    }
}