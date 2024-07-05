package com.mrpowergamerbr.nicolebot.commands.message

import live.shuuyu.discordinteraktions.common.commands.ApplicationCommandContext
import live.shuuyu.discordinteraktions.common.commands.MessageCommandExecutor
import live.shuuyu.discordinteraktions.common.entities.messages.Message

class ContentLengthMessageExecutor : MessageCommandExecutor() {
    override suspend fun execute(context: ApplicationCommandContext, targetMessage: Message) {
        context.sendEphemeralMessage {
            content = "The message has ${targetMessage.content?.length} characters!"
        }
    }
}