package com.mrpowergamerbr.nicolebot.commands.slash

import dev.kord.common.entity.TextInputStyle
import live.shuuyu.discordinteraktions.common.builder.message.embed
import live.shuuyu.discordinteraktions.common.modals.ModalContext
import live.shuuyu.discordinteraktions.common.modals.ModalExecutor
import live.shuuyu.discordinteraktions.common.modals.ModalExecutorDeclaration
import live.shuuyu.discordinteraktions.common.modals.components.ModalArguments
import live.shuuyu.discordinteraktions.common.modals.components.ModalComponents

class ModalYayExecutor : ModalExecutor {
    companion object : ModalExecutorDeclaration("modal_submit_example") {
        object Options : ModalComponents() {
            val something = textInput("something", TextInputStyle.Short)

            val somethingEvenBigger = optionalTextInput("something_even_bigger", TextInputStyle.Paragraph) {
                this.allowedLength = 20..100
            }
        }

        override val options = Options
    }

    override suspend fun onSubmit(
        context: ModalContext,
        args: ModalArguments
    ) {
        context.sendEphemeralMessage {
            content = "Done! You typed: ${args[Options.something]}"

            val somethingEvenBigger = args[Options.somethingEvenBigger]

            if (somethingEvenBigger != null) {
                embed {
                    title = "How's your day?"
                    description = args[Options.somethingEvenBigger]
                }
            }
        }
    }
}