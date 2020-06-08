package tech.lhzmrl.multi.hacker.netease

import tech.lhzmrl.multi.hacker.netease.model.request.EncryptedNeteaseRequest
import tech.lihz.multi.kotlin.extension.util.hexString2Bytes
import java.math.BigInteger
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val MODULES = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7"
const val NONCE = "0CoJUm6Qyw8W8jud"
const val PUB_KEY = "010001"

fun encryptRequest(request: String, secretKey: String): String {
    val paddingNumber = 16 - request.length % 16
    val sb = StringBuilder(request)
    for (i in (0 until paddingNumber)) {
        sb.append(paddingNumber.toChar())
    }
    val paddedRequest = sb.toString()
    //创建cipher对象
    val cipher = Cipher.getInstance("AES/CBC/NoPadding")
    //初始化:加密/解密
    val keySpec = SecretKeySpec(secretKey.toByteArray(), "AES")
    val iv = IvParameterSpec("0102030405060708".toByteArray())
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
    //加密
    val cipherText = cipher.doFinal(paddedRequest.toByteArray())
    return Base64.getEncoder().encodeToString(cipherText)
}

fun encryptSecretKey(secretKey: String): String {
    val reversedSecretKey = secretKey.reversed()
    val biText = BigInteger(1, reversedSecretKey.toByteArray())
    val biEx = BigInteger(1, PUB_KEY.hexString2Bytes())
    val biMod = BigInteger(1, MODULES.hexString2Bytes())
    val biRet = biText.modPow(biEx, biMod).toString(16)
    return biRet.padStart(256, 0.toChar())
}

actual fun encryptionNeteaseRequest(request: String): EncryptedNeteaseRequest {
    val secretKey = createSecretKey()
    val encryptedRequest = encryptRequest(encryptRequest(request, NONCE), secretKey)
    val encryptedSecretKey = encryptSecretKey(secretKey)

    return EncryptedNeteaseRequest(encryptedRequest, encryptedSecretKey)
}
