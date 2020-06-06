package tech.lhzmrl.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse {
    var code: Int = 0
    var message: String? = null
    var msg: String? = null
}