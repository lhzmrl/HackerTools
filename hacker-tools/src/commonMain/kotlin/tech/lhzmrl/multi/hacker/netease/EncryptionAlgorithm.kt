package tech.lhzmrl.multi.hacker.netease

import tech.lihz.multi.hacker.netease.model.request.EncryptedNeteaseRequest

fun createSecretKey(): String {
    val secretKey = StringBuilder()
    for (i in 0 until 16) {
        val random = (0 until 62).random()
        val c: Int = when {
            random < 10 -> random + '0'.toInt()
            random < 36 -> random - 10 + 'A'.toInt()
            else -> random - 36 + 'a'.toInt()
        }
        secretKey.append(c.toChar())
    }
    return secretKey.toString()
}

expect fun encryptionNeteaseRequest(request: String): EncryptedNeteaseRequest
