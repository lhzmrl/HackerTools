package tech.lhzmrl.multi.hacker.netease

import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import tech.lhzmrl.multi.hacker.netease.model.response.PlayListTrack
import tech.lihz.multi.logger.Logger

const val LISTEN_COUNT = 300

fun main() = runBlocking {
    val neteaseCloudMusic = NeteaseCloudMusic(OkHttp.create())
    var count = 0
    var delayTime = 0L
    val songs = ArrayList<PlayListTrack>()
    while (count < LISTEN_COUNT) {
        try {
            val personalFMSongs = neteaseCloudMusic.getPersonalFM()
            delay(delayTime)
            songs.addAll(personalFMSongs)
            personalFMSongs.forEach {
                Logger.i("听歌记录", it.name)
                count++
            }
            delayTime = 0
        } catch (e: Exception) {
            println(e)
            delayTime += 1000
        }
    }
    var i = 0
    var isSuccess = false
    delayTime = 2000
    while (i < 3) {
        delay(delayTime)
        try {
            if (neteaseCloudMusic.recordSongsPlayed(songs)) {
                isSuccess = true
            }
            i++
        } catch (e: Exception) {
            println(e)
            delayTime += 1000
        }
    }
    if (isSuccess) {
        println("上传成功")
    } else {
        println("上传失败")
    }
}