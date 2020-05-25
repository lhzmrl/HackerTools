package tech.lihz.multi.hacker.netease

import tech.lihz.multi.hacker.netease.model.Token

expect fun storeToken(token: Token): Boolean

expect fun resolveToken(): Token?

expect fun currentTimeMillis(): Long