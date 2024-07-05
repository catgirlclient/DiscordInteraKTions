package com.mrpowergamerbr.nicolebot.commands.user

import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import live.shuuyu.discordinteraktions.common.builder.message.embed
import live.shuuyu.discordinteraktions.common.commands.ApplicationCommandContext
import live.shuuyu.discordinteraktions.common.commands.UserCommandExecutor

class ViewAvatarUserExecutor : UserCommandExecutor() {
    override suspend fun execute(
        context: ApplicationCommandContext,
        targetUser: User,
        targetMember: Member?
    ) {
        context.sendEphemeralMessage {
            embed {
                image = (targetUser.avatar ?: targetUser.defaultAvatar).cdnUrl.toUrl()
            }
        }
    }
}