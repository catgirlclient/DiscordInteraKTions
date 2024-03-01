package com.mrpowergamerbr.nicolebot.commands.slash

import dev.kord.common.Color
import kotlinx.datetime.Clock
import net.perfectdreams.discordinteraktions.common.builder.message.embed
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class SendEmbedExecutor: SlashCommandExecutor() {
    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            embed {
                title = "Hello world!"
                description = "Hello world!"
                color = Color(0, 0, 0)
                timestamp = Clock.System.now()
            }
        }
    }
}