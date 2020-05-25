package tech.lihz.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
class RecommendResponse : BaseResponse() {
    var recommend: List<Recommend>? = null
}