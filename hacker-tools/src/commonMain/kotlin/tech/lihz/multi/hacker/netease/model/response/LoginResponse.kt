package tech.lihz.multi.hacker.netease.model.response

import kotlinx.serialization.Serializable

@Serializable
class LoginResponse : BaseResponse() {

    val token: String? = null

}