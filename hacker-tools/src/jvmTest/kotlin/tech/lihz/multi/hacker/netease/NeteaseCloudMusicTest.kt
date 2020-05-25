package tech.lihz.multi.hacker.netease

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.junit.Test

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
    fun testIncreaseListenedSongsCount() = runBlocking {
        increaseListenedSongsCount()
    }

    @Test
    fun testLoginIfNeeded() = runBlocking {
        neteaseCloudMusic.loginIfNeeded()
    }

}