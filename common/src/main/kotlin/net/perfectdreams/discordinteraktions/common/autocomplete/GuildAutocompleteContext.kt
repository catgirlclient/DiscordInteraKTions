package net.perfectdreams.discordinteraktions.common.autocomplete

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

public open class GuildAutocompleteContext(
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    arguments: List<CommandArgument<*>>,
    discordInteractionData: DiscordInteraction,
    public val guildId: Snowflake,
    public val member: Member
) : AutocompleteContext(sender, channelId, data, arguments, discordInteractionData) {
    public val appPermissions: Permissions = discordInteractionData.appPermissions.value ?: error("App Permissions field is null on a Guild Interaction! Bug?")
}