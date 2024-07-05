package live.shuuyu.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordAttachment
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.cache.data.MemberData
import dev.kord.core.cache.data.UserData
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import kotlinx.datetime.Instant
import live.shuuyu.discordinteraktions.common.entities.messages.Message

public open class KordMessage(public val kord: Kord, public val data: DiscordMessage) : Message {
    override val id: Snowflake by data::id
    override val channelId: Snowflake by data::channelId
    override val guildId: Snowflake?
        get() = data.guildId.value
    override val author: User
        get() = User(UserData.from(data.author), kord)
    override val member: Member?
        get() = data.member.value?.let {
            // I don't think the guildId is null if the member object is present
            Member(MemberData.from(author.id, guildId!!, it), author.data, kord)
        }
    override val content: String by data::content
    override val timestamp: Instant by data::timestamp
    override val editedTimestamp: Instant? by data::editedTimestamp
    override val attachments: List<DiscordAttachment> by data::attachments
}