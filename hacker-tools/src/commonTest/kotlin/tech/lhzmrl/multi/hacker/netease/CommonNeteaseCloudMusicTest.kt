package tech.lhzmrl.multi.hacker.netease

import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.delay
import tech.lihz.multi.hacker.netease.model.response.PlayListTrack

abstract class CommonNeteaseCloudMusicTest {

    abstract fun getHttpClientEngine(): HttpClientEngine

    val neteaseCloudMusic = NeteaseCloudMusic(getHttpClientEngine())

    protected suspend fun login() {
        neteaseCloudMusic.login(_root_ide_package_.tech.lhzmrl.multi.hacker.netease.AccountConfig.NAME, _root_ide_package_.tech.lhzmrl.multi.hacker.netease.AccountConfig.PASSWORD)
    }

    protected suspend fun recommend() {
        neteaseCloudMusic.recommend()
    }

    protected suspend fun loginAndRecommend() {
        neteaseCloudMusic.recommend()
    }

    protected suspend fun recordSongPlayed() {
        neteaseCloudMusic.recordSongsPlayed(ArrayList<PlayListTrack>())
    }

    protected suspend fun getPersonalFM() {
        neteaseCloudMusic.getPersonalFM()
    }

    protected suspend fun increaseListenedSongsCount() {
        val recommends = neteaseCloudMusic.recommend()
        val tracks = ArrayList<PlayListTrack>()
        for (recommend in recommends.reversed()) {
            try {
                val playList = neteaseCloudMusic.getPlayListDetail(recommend.id)
                delay(500)
                neteaseCloudMusic.recordSongsPlayed(playList.tracks!!)
                delay(500)
                tracks.addAll(playList.tracks!!)
                if (tracks.size > 300) {
                    break
                }
            } catch (e: Exception) {
                println(e)
            }
        }
        neteaseCloudMusic.recordSongsPlayed(tracks)
    }

    protected suspend fun recordTopListListened(id: Long) {
        val playList = neteaseCloudMusic.getPlayListDetail(id)
        println("==========================================" + playList.id + "==========================================")
        playList.tracks?.forEach {
            println(it.name)
        }
        neteaseCloudMusic.recordSongsPlayed(playList.tracks!!)
    }

}