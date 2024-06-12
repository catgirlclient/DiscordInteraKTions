package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage

public class KordPublicFollowupMessage(
    kord: Kord,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    handle: DiscordMessage
) : KordPublicMessage(kord, handle), EditableMessage {
    override val id: Snowflake = handle.id
    override val content: String by handle::content

    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = kord.rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            data.id,
            message.toFollowupMessageModifyBuilder().toRequest()
        )

        return KordPublicFollowupMessage(
            kord,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}