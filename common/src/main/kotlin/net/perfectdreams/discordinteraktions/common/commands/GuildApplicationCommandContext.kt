package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

public open class GuildApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    discordInteractionData: DiscordInteraction,
    applicationCommandDeclaration: ApplicationCommandDeclaration,
    public val guildId: Snowflake,
    public val member: Member
) : ApplicationCommandContext(bridge, sender, channelId, data, discordInteractionData, applicationCommandDeclaration) {
    /**
     * Returns the [Permissions] of the application.
     */
    public val appPermissions: Permissions = discordInteractionData.appPermissions.value ?: error("App Permissions field is null on a Guild Interaction! Bug?")
}