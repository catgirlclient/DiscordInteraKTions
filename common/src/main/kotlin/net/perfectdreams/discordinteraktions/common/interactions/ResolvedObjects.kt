package net.perfectdreams.discordinteraktions.common.interactions

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Attachment
import dev.kord.core.entity.Member
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.ResolvedChannel
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

public class ResolvedObjects (
    public val channels: Map<Snowflake, ResolvedChannel>?,
    public val roles: Map<Snowflake, Role>?,
    public val users: Map<Snowflake, User>?,
    public val members: Map<Snowflake, Member>?,
    public val messages: Map<Snowflake, Message>?,
    public val attachments: Map<Snowflake, Attachment>?
)