package live.shuuyu.discordinteraktions.common.modals

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import live.shuuyu.discordinteraktions.common.interactions.InteractionData
import live.shuuyu.discordinteraktions.common.requests.RequestBridge

public open class GuildModalContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    modalExecutorDeclaration: ModalExecutorDeclaration,
    dataOrNull: String?,
    data: InteractionData,
    discordInteractionData: DiscordInteraction,
    public val guildId: Snowflake,
    public val member: Member
) : ModalContext(bridge, sender, channelId, modalExecutorDeclaration, dataOrNull, data, discordInteractionData) {
    public val appPermissions: Permissions = discordInteractionData.appPermissions.value ?: error("App Permissions field is null on a Guild Interaction! Bug?")
}