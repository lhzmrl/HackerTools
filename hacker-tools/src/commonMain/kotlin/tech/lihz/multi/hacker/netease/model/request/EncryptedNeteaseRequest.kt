package tech.lihz.multi.hacker.netease.model.request

import kotlinx.serialization.Serializable

@Serializable
data class EncryptedNeteaseRequest(val params: String, val encSecKey: String)