package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.SendEmbedExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object EmbedCommand: SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("embed", "Sends a test embed.") {
        executor = SendEmbedExecutor()
    }
}