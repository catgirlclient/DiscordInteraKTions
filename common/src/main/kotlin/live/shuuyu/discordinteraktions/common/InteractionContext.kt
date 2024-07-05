package live.shuuyu.discordinteraktions.common

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import live.shuuyu.discordinteraktions.common.interactions.InteractionData
import live.shuuyu.discordinteraktions.common.requests.RequestBridge

public open class InteractionContext(
    bridge: RequestBridge,
    public val sender: User,
    public val channelId: Snowflake,
    public val interactionData: InteractionData,

    /**
     * The interaction data object from Discord, useful if you need to use data that is not exposed directly via Discord InteraKTions
     */
    public val discordInteraction: DiscordInteraction
) : live.shuuyu.discordinteraktions.common.BarebonesInteractionContext(bridge)