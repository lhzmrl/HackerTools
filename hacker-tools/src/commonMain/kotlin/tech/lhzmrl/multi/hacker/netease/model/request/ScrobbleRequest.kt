package tech.lhzmrl.multi.hacker.netease.model.request

import kotlinx.serialization.Serializable

@Serializable
class ScrobbleRequest {
    var id = 0L
    var sourceId = 0L
    var time = 0
}