package net.perfectdreams.discordinteraktions.common.modals

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.InteractionContext
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge

public open class ModalContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    public val modalExecutorDeclaration: ModalExecutorDeclaration,
    public val dataOrNull: String?,
    data: InteractionData,
    discordInteractionData: DiscordInteraction
) : InteractionContext(bridge, sender, channelId, data, discordInteractionData) {
    public val data: String
        get() = dataOrNull ?: error("There isn't any custom data present in this modal submit context!")
}