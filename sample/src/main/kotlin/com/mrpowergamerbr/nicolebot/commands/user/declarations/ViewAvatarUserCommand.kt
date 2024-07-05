package com.mrpowergamerbr.nicolebot.commands.user.declarations

import com.mrpowergamerbr.nicolebot.commands.user.ViewAvatarUserExecutor
import live.shuuyu.discordinteraktions.common.commands.UserCommandDeclarationWrapper
import live.shuuyu.discordinteraktions.common.commands.userCommand

object ViewAvatarUserCommand : UserCommandDeclarationWrapper {
    override fun declaration() = userCommand("View avatar", ViewAvatarUserExecutor())
}