package net.perfectdreams.discordinteraktions.common

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class InteractionContext(
    bridge: RequestBridge,
    val sender: User,
    val channelId: OptionalSnowflake,
    val interactionData: InteractionData,

    /**
     * The interaction data object from Discord, useful if you need to use data that is not exposed directly via Discord InteraKTions
     */
    val discordInteraction: DiscordInteraction
) : BarebonesInteractionContext(bridge)