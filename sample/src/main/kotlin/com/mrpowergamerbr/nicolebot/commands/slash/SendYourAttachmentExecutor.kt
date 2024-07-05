package com.mrpowergamerbr.nicolebot.commands.slash

import live.shuuyu.discordinteraktions.common.commands.ApplicationCommandContext
import live.shuuyu.discordinteraktions.common.commands.SlashCommandExecutor
import live.shuuyu.discordinteraktions.common.commands.options.ApplicationCommandOptions
import live.shuuyu.discordinteraktions.common.commands.options.SlashCommandArguments

class SendYourAttachmentExecutor : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
        val attachment = attachment("attachment", "Your attachment")
    }

    override val options = Options()

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            // The attachment URL is ephemeral, so you need to reupload it somewhere else if you want to persist it!
            content = "You sent ${args[options.attachment].url} to me!"
        }
    }
}