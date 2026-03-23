package com.cecbrain.utils.crypto

import android.net.Network
import android.util.Base64
import com.cecbrain.utils.network.NetworkUtil
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesUtils {
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val TAG_LENGTH = 128

    /**
     * @param key 16位或32位密钥
     * @param iv 12位初始化向量（建议随机生成并随密文存储）
     */
    fun encrypt(data: String, key: String, iv: ByteArray): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val gcmSpec = GCMParameterSpec(TAG_LENGTH, iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(encryptedBase64: String, key: String, iv: ByteArray): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val gcmSpec = GCMParameterSpec(TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
        val decoded = Base64.decode(encryptedBase64, Base64.DEFAULT)
        return String(cipher.doFinal(decoded))
    }
}