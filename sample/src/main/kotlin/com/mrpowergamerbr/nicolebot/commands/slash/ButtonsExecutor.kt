package com.mrpowergamerbr.nicolebot.commands.slash

import dev.kord.common.entity.ButtonStyle
import live.shuuyu.discordinteraktions.common.builder.message.actionRow
import live.shuuyu.discordinteraktions.common.commands.ApplicationCommandContext
import live.shuuyu.discordinteraktions.common.commands.SlashCommandExecutor
import live.shuuyu.discordinteraktions.common.commands.options.SlashCommandArguments
import live.shuuyu.discordinteraktions.common.components.interactionButton

class ButtonsExecutor : SlashCommandExecutor() {
    override suspend fun execute(
        context: ApplicationCommandContext,
        args: SlashCommandArguments
    ) {
        context.sendMessage {
            content = "Click on a button!"

            actionRow {
                interactionButton(
                    ButtonStyle.Primary,
                    FancyButtonClickExecutor,
                    "lori is so cute!! :3"
                ) {
                    label = "Fancy Button!"
                }

            }
        }
    }

}