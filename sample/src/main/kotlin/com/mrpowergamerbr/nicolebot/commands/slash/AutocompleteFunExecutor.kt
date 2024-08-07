package com.mrpowergamerbr.nicolebot.commands.slash

import live.shuuyu.discordinteraktions.common.commands.ApplicationCommandContext
import live.shuuyu.discordinteraktions.common.commands.SlashCommandExecutor
import live.shuuyu.discordinteraktions.common.commands.options.ApplicationCommandOptions
import live.shuuyu.discordinteraktions.common.commands.options.SlashCommandArguments

class AutocompleteFunExecutor : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
        val text = string("text", "Text")

        val autocompleteText = string("autocomplete_text", "Autocomplete Text") {
            autocomplete { context, focused ->
                val filledValue = focused.value

                // We can get the value that the user has already filled in other options
                // If the user didn't fill the "text" option yet, we will return an empty choice map
                val textOptionValue = context.getArgument(options.text) ?: return@autocomplete mapOf()

                // Description -> Value
                // You can send 25 autocomplete fields
                return@autocomplete mapOf(
                    "You typed $filledValue" to filledValue,
                    "You typed $textOptionValue" to textOptionValue,
                    "You typed ${textOptionValue.reversed()}" to textOptionValue.reversed()
                )
            }
        }
    }

    override val options = Options()

    override suspend fun execute(
        context: ApplicationCommandContext,
        args: SlashCommandArguments
    ) {
        context.sendEphemeralMessage {
            content = "You typed ${args[options.text]} and ${args[options.autocompleteText]}"
        }
    }
}