package tech.lhzmrl.multi.hacker.netease

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Cookie
import io.ktor.http.Parameters
import io.ktor.http.parseServerSetCookieHeader
import io.ktor.util.toMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import tech.lhzmrl.multi.hacker.netease.model.Token
import tech.lhzmrl.multi.hacker.netease.model.request.*
import tech.lhzmrl.multi.hacker.netease.model.response.*
import tech.lihz.multi.kotlin.extension.security.md5
import tech.lihz.multi.logger.DebugPrinter
import tech.lihz.multi.logger.Logger
import kotlin.collections.set
import kotlin.coroutines.CoroutineContext

class NeteaseCloudMusic(httpClientEngine: HttpClientEngine) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Unconfined

    private val httpClient: HttpClient

    private var token: Token

    init {
        Logger.install(DebugPrinter())
        httpClient = HttpClient(httpClientEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json(JsonConfiguration(ignoreUnknownKeys = true)))
            }
            install(Logging) {
                logger = object : io.ktor.client.features.logging.Logger {
                    override fun log(message: String) {
                        Logger.i("Ktor", message)
                    }
                }
                level = LogLevel.NONE
            }
        }
        val rToken = resolveToken()
        token = if (rToken == null) {
            Token()
        } else {
            rToken
        }
    }

    suspend fun login(userName: String, password: String) {
        if (userName.isEmpty() || password.isEmpty()) {
            throw Exception("请先在AccountConfig中配置用户名和密码")
        }
        val loginRequest = LoginRequest(userName, "86", password.md5.toLowerCase(), true)
        val loginResponse = jsonRequest<HttpResponse, LoginRequest>("https://music.163.com/weapi/login/cellphone?csrf_token=", loginRequest, LoginRequest.serializer())
        val cookies = loginResponse.headers.toMap().get("set-cookie")
        val cookieMap = HashMap<String, Cookie>(cookies?.size?:0)
        cookies?.forEach {
            val cookie = parseServerSetCookieHeader(it)
            cookieMap[cookie.name] = cookie
        }
        token.MUSIC_U = cookieMap["MUSIC_U"]?.value
        token.__remember_me = cookieMap["__remember_me"]?.value
        token.__csrf = cookieMap["__csrf"]?.value
        token.expires = cookieMap["__csrf"]?.expires!!.timestamp
        storeToken(token)
    }

    suspend fun loginIfNeeded() {
        if (token.expires >= currentTimeMillis()) {
            return
        }
        login(AccountConfig.NAME, AccountConfig.PASSWORD)
    }

    suspend fun recommend(): List<Recommend> {
        loginIfNeeded()
        val recommendRequest = RecommendRequest(token.__csrf!!)
        val recommendResponse = jsonRequest<RecommendResponse, RecommendRequest>("https://music.163.com/weapi/v1/discovery/recommend/resource", recommendRequest, RecommendRequest.serializer())
        return recommendResponse.recommend!!
    }

    suspend fun getPlayListDetail(id: Long): PlayList {
        loginIfNeeded()
        val playListDetailRequest = PlayListDetailRequest(id, 1000, token.__csrf!!)
        val playListDetailResponse = jsonRequest<PlayListDetailResponse, PlayListDetailRequest>("https://music.163.com/weapi/v3/playlist/detail", playListDetailRequest, PlayListDetailRequest.serializer())
        return playListDetailResponse.playlist!!
    }

    suspend fun recordSongsPlayed(tracks: List<PlayListTrack>): Boolean {
        loginIfNeeded()
        val json = Json(JsonConfiguration.Stable)
        val webLogRequest = WebLogRequest()
        val logs = ArrayList<WebLog>()
        tracks.forEach {
            val playLog = PlayLog()
            playLog.end = PlayStatus.playend
            playLog.id = it.id
            playLog.time = 240
            playLog.type = "song"
            playLog.wifi = 0
            playLog.download = 0

            val log = WebLog()
            log.action = "play"
            log.json = playLog
            logs.add(log)
        }
        webLogRequest.logs = json.stringify(WebLog.serializer().list, logs)
        val recordWebLogResponse = jsonRequest<WebLogResponse, WebLogRequest>("https://music.163.com/weapi/feedback/weblog", webLogRequest, WebLogRequest.serializer())
        return recordWebLogResponse.code == 200
    }

    suspend fun getPersonalFM(): List<PlayListTrack> {
        loginIfNeeded()
        val recommendRequest = RecommendRequest(token.__csrf!!)
        val response = jsonRequest<PersonalFMResponse, RecommendRequest>("https://music.163.com/weapi/v1/radio/get", recommendRequest, RecommendRequest.serializer())
        return response.data?:ArrayList(0)
    }

    private suspend inline fun <reified T, R> jsonRequest(url: String, request: R, serializer: SerializationStrategy<R>): T {
        val json = Json(JsonConfiguration.Stable)
        val encryptedNeteaseRequest = encryptionNeteaseRequest(json.stringify(serializer, request))
        return httpClient.submitForm<T>(Parameters.build {
            append("params", encryptedNeteaseRequest.params)
            append("encSecKey", encryptedNeteaseRequest.encSecKey)
        }, false, getHttpRequestBuilder(url))
    }

    private fun getHttpRequestBuilder(url: String): HttpRequestBuilder.() -> Unit = {
        url(url)
        header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,und;q=0.6")
        header("sec-fetch-dest", "empty")
        header("sec-fetch-mode", "cors")
        header("sec-fetch-site", "same-origin")
        header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
        header("Cookie", "MUSIC_U=${token.MUSIC_U}; __remember_me=${token.__remember_me}; __csrf=${token.__csrf}")
    }

}