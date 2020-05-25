package tech.lihz.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
class PersonalFMResponse: BaseResponse() {

    var data: List<PlayListTrack>? = null

}