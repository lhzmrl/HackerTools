package tech.lihz.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
class PlayListTrack {
    var id: Long = 0L
    var name: String? = null
    var al: Album? = null
}