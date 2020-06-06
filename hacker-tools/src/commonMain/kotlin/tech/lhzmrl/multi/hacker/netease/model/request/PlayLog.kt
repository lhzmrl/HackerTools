package tech.lhzmrl.multi.hacker.netease.model.request

import kotlinx.serialization.Serializable

@Serializable
class PlayLog {
    var type: String? = "song"
    var wifi = 0
    var download = 0
    var id: Long = 0
    var time = 0
    var end: PlayStatus? = PlayStatus.playend
    var source: PlaySource? = null
    var sourceId: String? = null

}