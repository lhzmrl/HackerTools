package tech.lhzmrl.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
class PlayList {
    var id: Long? = 0
    var trackIds: List<PlayListTrackId>? = null
    var tracks: List<PlayListTrack>? = null
}