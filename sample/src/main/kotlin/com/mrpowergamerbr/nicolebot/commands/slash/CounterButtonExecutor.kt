package com.mrpowergamerbr.nicolebot.commands.slash

import com.mrpowergamerbr.nicolebot.utils.Counter
import dev.kord.core.entity.User
import live.shuuyu.discordinteraktions.common.components.ButtonExecutor
import live.shuuyu.discordinteraktions.common.components.ButtonExecutorDeclaration
import live.shuuyu.discordinteraktions.common.components.ComponentContext

class CounterButtonExecutor(private val counter: Counter) : ButtonExecutor {
    // All buttons must have unique IDs!
    companion object : ButtonExecutorDeclaration("counter")

    override suspend fun onClick(user: User, context: ComponentContext) {
        val newCount = counter.addAndGet()

        // The order is important here!
        // If you send the message first and then update the message, Discord will think that you want to edit the message that you sent!
        context.updateMessage(CounterExecutor.createCounterMessage(newCount))

        context.sendEphemeralMessage {
            content = "Increased the counter!"
        }
    }
}