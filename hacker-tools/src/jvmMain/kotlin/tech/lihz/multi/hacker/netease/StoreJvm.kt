package tech.lihz.multi.hacker.netease

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import tech.lihz.multi.hacker.netease.model.Token
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter

actual fun storeToken(token: Token): Boolean {
    val file = File("token")
    if (!file.exists()) {
        val cr = file.createNewFile()
        if (!cr) {
            return false
        }
    }
    val fw = FileWriter(file)
    val json = Json(JsonConfiguration.Stable)
    fw.write(json.stringify(Token.serializer(), token))
    fw.close()
    return true
}

actual fun resolveToken(): Token? {
    val file = File("token")
    if (!file.exists()) {
        return null
    }
    val fr = FileReader(file)
    val content = fr.readText()
    if (content.isEmpty()) {
        return null
    }
    val json = Json(JsonConfiguration.Stable)
    val token = json.parse(Token.serializer(), content)
    fr.close()
    return token
}

actual fun currentTimeMillis(): Long {
    return System.currentTimeMillis()
}