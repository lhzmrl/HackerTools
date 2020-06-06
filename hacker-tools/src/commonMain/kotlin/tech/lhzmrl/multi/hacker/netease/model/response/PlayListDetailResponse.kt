package tech.lhzmrl.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
class PlayListDetailResponse : BaseResponse() {
    var playlist: PlayList? = null
}
