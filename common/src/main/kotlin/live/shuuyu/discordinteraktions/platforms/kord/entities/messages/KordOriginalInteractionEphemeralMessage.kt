package live.shuuyu.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import live.shuuyu.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import live.shuuyu.discordinteraktions.common.entities.messages.EditableMessage
import live.shuuyu.discordinteraktions.common.entities.messages.EphemeralMessage

public class KordOriginalInteractionEphemeralMessage(
    private val kord: Kord,
    private val applicationId: Snowflake,
    private val interactionToken: String
) : EphemeralMessage, EditableMessage, OriginalInteractionMessage() {
    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = kord.rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            message.toInteractionMessageResponseModifyBuilder().toRequest()
        )

        return KordEditedOriginalInteractionEphemeralMessage(
            kord,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}