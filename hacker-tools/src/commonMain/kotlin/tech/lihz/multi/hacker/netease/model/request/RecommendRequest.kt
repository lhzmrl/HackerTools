package tech.lihz.multi.hacker.netease.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RecommendRequest(val csrf_token: String)