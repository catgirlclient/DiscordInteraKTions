package com.mrpowergamerbr.nicolebot.commands.message.declarations

import com.mrpowergamerbr.nicolebot.commands.message.ContentLengthMessageExecutor
import live.shuuyu.discordinteraktions.common.commands.MessageCommandDeclarationWrapper
import live.shuuyu.discordinteraktions.common.commands.messageCommand

object ContentLengthMessageCommand : MessageCommandDeclarationWrapper {
    override fun declaration() = messageCommand("View content length", ContentLengthMessageExecutor())
}