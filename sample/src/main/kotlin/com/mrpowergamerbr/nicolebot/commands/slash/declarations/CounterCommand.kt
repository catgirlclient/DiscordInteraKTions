package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.CounterExecutor
import com.mrpowergamerbr.nicolebot.utils.Counter
import live.shuuyu.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import live.shuuyu.discordinteraktions.common.commands.slashCommand

class CounterCommand(val counter: Counter) : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("counter", "Click to increase the counter!") {
        executor = CounterExecutor(counter)
    }
}