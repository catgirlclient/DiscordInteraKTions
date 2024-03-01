package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.common.entity.DiscordAttachment
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import kotlinx.datetime.Instant

public interface Message {
    public val id: Snowflake
    public val channelId: Snowflake
    public val guildId: Snowflake?
    public val author: User
    public val member: Member?
    public val content: String?
    public val timestamp: Instant
    public val editedTimestamp: Instant?
    public val attachments: List<DiscordAttachment>
}