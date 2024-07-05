package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.HelloWorldExecutor
import live.shuuyu.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import live.shuuyu.discordinteraktions.common.commands.slashCommand

object HelloWorldCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("helloworld", "Hello, World!") {
        executor = HelloWorldExecutor()
    }
}