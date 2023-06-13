package net.perfectdreams.discordinteraktions.common.interactions

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Attachment
import dev.kord.core.entity.Member
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.ResolvedChannel
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

class ResolvedObjects(
    val channels: Map<Snowflake, ResolvedChannel>?,
    val roles: Map<Snowflake, Role>?,
    val users: Map<Snowflake, User>?,
    val members: Map<Snowflake, Member>?,
    val messages: Map<Snowflake, Message>?,
    val attachments: Map<Snowflake, Attachment>?
)