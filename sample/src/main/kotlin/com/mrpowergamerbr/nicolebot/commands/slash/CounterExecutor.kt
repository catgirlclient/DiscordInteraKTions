package com.mrpowergamerbr.nicolebot.commands.slash

import com.mrpowergamerbr.nicolebot.utils.Counter
import dev.kord.common.entity.ButtonStyle
import live.shuuyu.discordinteraktions.common.builder.message.MessageBuilder
import live.shuuyu.discordinteraktions.common.builder.message.actionRow
import live.shuuyu.discordinteraktions.common.commands.ApplicationCommandContext
import live.shuuyu.discordinteraktions.common.commands.SlashCommandExecutor
import live.shuuyu.discordinteraktions.common.commands.options.SlashCommandArguments
import live.shuuyu.discordinteraktions.common.components.interactiveButton

class CounterExecutor(private val counter: Counter) : SlashCommandExecutor() {
    companion object {
        fun createCounterMessage(currentCount: Int): MessageBuilder.() -> (Unit) = {
            content = """Current count: $currentCount
                |
                |Click the button to increase the counter!
            """.trimMargin()

            actionRow {
                interactiveButton(
                    ButtonStyle.Primary,
                    CounterButtonExecutor
                ) {
                    label = "Increase the counter!"
                }
            }
        }
    }

    override suspend fun execute(
        context: ApplicationCommandContext,
        args: SlashCommandArguments
    ) {
        val currentCount = counter.get()

        context.sendMessage {
            apply(createCounterMessage(currentCount))
        }
    }
}