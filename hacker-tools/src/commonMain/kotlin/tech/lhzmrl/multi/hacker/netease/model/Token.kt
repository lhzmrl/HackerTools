package tech.lhzmrl.multi.hacker.netease.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(var MUSIC_U: String? = null,
                 var __remember_me: String? = null,
                 var __csrf: String? = null,
                 var expires: Long = 0L)