package tech.lhzmrl.multi.hacker.netease.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PlayListDetailRequest(var id: Long, var n: Int, var csrf_token: String)