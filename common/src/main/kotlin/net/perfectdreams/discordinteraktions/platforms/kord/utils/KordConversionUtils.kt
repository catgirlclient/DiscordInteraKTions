package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ResolvedObjects
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.core.Kord
import dev.kord.core.cache.data.*
import dev.kord.core.entity.Attachment
import dev.kord.core.entity.Member
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.ResolvedChannel
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordMessage

/**
 * Converts Kord's Resolved Objects to Discord InteraKTions's Resolved Objects
 */
fun ResolvedObjects.toDiscordInteraKTionsResolvedObjects(kord: Kord, guildId: Snowflake?): net.perfectdreams.discordinteraktions.common.interactions.ResolvedObjects {
    val channels = this.channels.value?.map {
        it.key to ResolvedChannel(
            ChannelData.from(it.value),
            kord
        )
    }?.toMap()

    val roles = this.roles.value?.map {
        it.key to Role(
            RoleData.from(
                guildId ?: error("Guild ID is null, however there are members present in the resolved objects list! Bug?"),
                it.value
            ),
            kord
        )
    }?.toMap()

    val users = this.users.value?.map {
        it.key to User(UserData.from(it.value), kord)
    }?.toMap()

    val members = this.members.value?.map {
        // In this case, the user map contains the user object, so we need to get it from there
        val user = users?.get(it.key) ?: error("Couldn't find a user ${it.key} reference in the users map!")

        it.key to Member(
            MemberData.from(
                user.id, // Should NEVER be null!
                guildId ?: error("Guild ID is null, however there are members present in the resolved objects list! Bug?"),
                it.value
            ),
            user.data,
            kord
        )
    }?.toMap()

    val messages = this.messages.value?.map {
        it.key to KordMessage(
            kord,
            it.value
        )
    }?.toMap()

    val attachments = this.attachments.value?.map {
        it.key to Attachment (
            AttachmentData.from(
                it.value
            ),
            kord
        )
    }?.toMap()

    return net.perfectdreams.discordinteraktions.common.interactions.ResolvedObjects(
        channels,
        roles,
        users,
        members,
        messages,
        attachments
    )
}

fun <T> runIfNotMissing(optional: Optional<T>, callback: (T?) -> (Unit)) {
    if (optional !is Optional.Missing)
        callback.invoke(optional.value)
}