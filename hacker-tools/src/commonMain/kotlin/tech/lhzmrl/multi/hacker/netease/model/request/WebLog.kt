package tech.lhzmrl.multi.hacker.netease.model.request

import kotlinx.serialization.Serializable

@Serializable
class WebLog {
    var action: String? = null
    var json: PlayLog? = null
}