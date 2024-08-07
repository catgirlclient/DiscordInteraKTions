package com.mrpowergamerbr.nicolebot

import com.mrpowergamerbr.nicolebot.commands.message.declarations.ContentLengthMessageCommand
import com.mrpowergamerbr.nicolebot.commands.slash.CounterButtonExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.FancyButtonClickExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.ModalYayExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.declarations.*
import com.mrpowergamerbr.nicolebot.commands.user.declarations.ViewAvatarUserCommand
import com.mrpowergamerbr.nicolebot.utils.Counter
import com.mrpowergamerbr.nicolebot.utils.LanguageManager
import dev.kord.common.entity.Snowflake
import live.shuuyu.discordinteraktions.common.DiscordInteraKTions
import java.io.File

// "Nicole" is an easter egg: Loritta's (https://loritta.website/) name was planned to be "Nicole" at one point
class NicoleBot(token: String) {
    companion object {
        val APPLICATION_ID = Snowflake(File("applicationid.txt").readText())
        val GUILD_ID = Snowflake(File("guildid.txt").readText())
    }

    val interaKTions = DiscordInteraKTions(token, APPLICATION_ID)
    val counter = Counter(0)
    private val languageManager = LanguageManager()

    suspend fun registerCommands() {
        // ===[ /helloworld ]===
        interaKTions.manager.register(HelloWorldCommand)

        // ===[ /options ]===
        interaKTions.manager.register(OptionsCommand)

        // ===[ /interactivity ]===
        interaKTions.manager.register(InteractivityCommand)
        interaKTions.manager.register(FancyButtonClickExecutor())

        interaKTions.manager.register(
            ModalYayExecutor,
            ModalYayExecutor()
        )

        // ===[ /embed ]===
        interaKTions.manager.register(EmbedCommand)

        // ===[ /counter ]===
        interaKTions.manager.register(CounterCommand(counter))
        interaKTions.manager.register(CounterButtonExecutor(counter))

        // ===[ /sendyourattachment ]===
        interaKTions.manager.register(SendYourAttachmentCommand)

        // ===[ /autocompletefun ]===
        interaKTions.manager.register(AutocompleteFunCommand)

        // ===[ /externallyprovidedstring ]===
        interaKTions.manager.register(ExternallyProvidedStringCommand(languageManager))

        // ===[ "View avatar" ]===
        interaKTions.manager.register(ViewAvatarUserCommand)

        // ===[ "View content length" ]===
        interaKTions.manager.register(ContentLengthMessageCommand)

        interaKTions.updateAllCommandsInGuild(GUILD_ID)
        // registry.updateAllGlobalCommands()
    }
}