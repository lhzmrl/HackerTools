package tech.lhzmrl.multi.hacker.netease.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val phone: String, val countrycode:String, val password: String, val rememberLogin: Boolean)