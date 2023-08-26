package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: OptionalSnowflake,
    data: InteractionData,
    discordInteractionData: DiscordInteraction,
    applicationCommandDeclaration: ApplicationCommandDeclaration,
    val guildId: Snowflake,
    val member: Member
) : ApplicationCommandContext(bridge, sender, channelId, data, discordInteractionData, applicationCommandDeclaration) {
    val appPermissions = discordInteractionData.appPermissions.value ?: error("App Permissions field is null on a Guild Interaction! Bug?")
}