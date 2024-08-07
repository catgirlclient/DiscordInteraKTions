package com.mrpowergamerbr.nicolebot.commands.slash

import com.mrpowergamerbr.nicolebot.utils.customoptions.delayedSuspendable
import live.shuuyu.discordinteraktions.common.commands.ApplicationCommandContext
import live.shuuyu.discordinteraktions.common.commands.SlashCommandExecutor
import live.shuuyu.discordinteraktions.common.commands.options.ApplicationCommandOptions
import live.shuuyu.discordinteraktions.common.commands.options.SlashCommandArguments

class DSCustomOptionsExecutor : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
        val delayedSuspendable = delayedSuspendable("delayed_suspendable", "This option is delayed and suspendable")
    }

    override val options = Options()

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.deferChannelMessage()

        // Yeah, it would've been nicer if it was more "seamless" instead of manually calling ".parse()" on a suspendable method
        // But there isn't a good way to handle this: Map getter operators can't be suspendable, so creating an extension method for
        // "args[Options.delayedSuspendable]" wouldn't have worked.
        // And trying to force everything on a single object also wouldn't have worked, what if you want to provide additional context when
        // parsing an option? (Example: i18n keys)
        val result = args[options.delayedSuspendable].parse()

        context.sendMessage {
            content = buildString {
                append("Result: $result")
            }
        }
    }
}