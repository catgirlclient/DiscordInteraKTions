package com.mrpowergamerbr.nicolebot

import dev.kord.gateway.DefaultGateway
import dev.kord.gateway.start
import kotlinx.coroutines.runBlocking
import live.shuuyu.discordinteraktions.platforms.kord.installDiscordInteraKTions
import java.io.File

object NicoleBotGatewayLauncher {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val token = File("token.txt").readText()

        val nicoleBot = NicoleBot(token)
        nicoleBot.registerCommands()

        val gateway = DefaultGateway {}
        gateway.installDiscordInteraKTions(nicoleBot.interaKTions)

        gateway.start(token)
    }
}