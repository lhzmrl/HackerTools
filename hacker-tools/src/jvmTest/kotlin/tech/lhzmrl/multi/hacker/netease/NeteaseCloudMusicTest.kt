package tech.lhzmrl.multi.hacker.netease

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import tech.lhzmrl.multi.hacker.netease.model.response.PlayListTrack
import tech.lihz.multi.logger.Logger

class NeteaseCloudMusicTest : CommonNeteaseCloudMusicTest() {

    override fun getHttpClientEngine(): HttpClientEngine {
        return OkHttp.create()
    }

    @Test
    fun testLogin() = runBlocking {
        login()
    }

    @Test
    fun testRecommend() = runBlocking {
        recommend()
    }

    @Test
    fun testLoginAndRecommend() = runBlocking {
        loginAndRecommend()
    }

    @Test
    fun testRecordSongPlayed() = runBlocking {
        recordSongPlayed()
    }

    @Test
    fun testGetPersonalFM() = runBlocking {
        getPersonalFM()
    }

    @Test
    fun testLoginIfNeeded() = runBlocking {
        neteaseCloudMusic.loginIfNeeded()
    }

    // 通过每日推荐歌单提升
    @Test
    fun testIncreaseListenedSongsCount() = runBlocking {
        increaseListenedSongsCount()
    }

    val LISTEN_COUNT = 300

    /**
     * 通过私人FM提升
     */
    @Test
    fun testIncreaseListenedSongsByFM() = runBlocking {
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

    /**
     * 通过歌曲榜单提升，榜单ID见 https://github.com/Binaryify/NeteaseCloudMusicApi/blob/master/module/top_list.js
     */
    @Test
    fun testTopList() = runBlocking {
        // 云音乐新歌榜
//        recordTopListListened(3779629)
        // 美国Billboard周榜
//        recordTopListListened(60198)
//        // 抖音排行榜
//        recordTopListListened(2250011882)
//        // 云音乐韩语榜
//        recordTopListListened(745956260)
//        // 日本Oricon周榜
//        recordTopListListened(60131)
//        // 华语金曲榜
//        recordTopListListened(4395559)
//        // iTunes榜
//        recordTopListListened(11641012)
//        // 美国Billboard周榜
//        recordTopListListened(60198)
//        // Hit FM Top榜
//        recordTopListListened(120001)
//        // 台湾Hito排行榜
//        recordTopListListened(112463)
        // Beatport全球电子舞曲榜
        recordTopListListened(3812895)
    }

}