package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.AutocompleteFunExecutor
import live.shuuyu.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import live.shuuyu.discordinteraktions.common.commands.slashCommand

object AutocompleteFunCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("autocompletefun", "Fun with Autocomplete") {
        executor = AutocompleteFunExecutor()
    }
}