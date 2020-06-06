package tech.lhzmrl.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
class PersonalFMResponse: BaseResponse() {

    var data: List<PlayListTrack>? = null

}