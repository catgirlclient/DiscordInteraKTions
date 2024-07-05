package live.shuuyu.discordinteraktions.common.requests.managers

import dev.kord.common.entity.Choice
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.builder.interaction.ModalBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import live.shuuyu.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import live.shuuyu.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import live.shuuyu.discordinteraktions.common.entities.messages.EditableMessage
import live.shuuyu.discordinteraktions.common.requests.InteractionRequestState
import live.shuuyu.discordinteraktions.common.requests.RequestBridge
import live.shuuyu.discordinteraktions.platforms.kord.entities.messages.KordEphemeralFollowupMessage
import live.shuuyu.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionPublicMessage
import live.shuuyu.discordinteraktions.platforms.kord.entities.messages.KordPublicFollowupMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 */
public class HttpRequestManager(
    bridge: RequestBridge,
    public val kord: Kord,
    public val applicationId: Snowflake,
    public val interactionToken: String
) : RequestManager(bridge) {
    public companion object {
        private val logger = KotlinLogging.logger {}
    }

    init {
        require(bridge.state.value != InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager shouldn't be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferChannelMessage(): Nothing = error("Can't defer a interaction that was already deferred!")

    override suspend fun deferChannelMessageEphemerally(): Nothing = error("Can't defer a interaction that was already deferred!")

    override suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = kord.rest.interaction.createFollowupMessage(
            applicationId,
            interactionToken,
            message.toFollowupMessageCreateBuilder().toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordPublicFollowupMessage(
            kord,
            applicationId,
            interactionToken,
            kordMessage
        )
    }

    override suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = kord.rest.interaction.createFollowupMessage(
            applicationId,
            interactionToken,
            message.toFollowupMessageCreateBuilder().toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordEphemeralFollowupMessage(
            kord,
            applicationId,
            interactionToken,
            kordMessage
        )
    }

    override suspend fun deferUpdateMessage(): Nothing = error("Can't defer a interaction that was already deferred!")

    override suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val interactionMessage = KordOriginalInteractionPublicMessage(
            kord,
            applicationId,
            interactionToken
        )

        val newMessage = interactionMessage.editMessage(message)

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return newMessage
    }

    override suspend fun sendStringAutocomplete(list: List<Choice.StringChoice>): Nothing =
        error("Can't send a autocomplete request via the HttpRequestManager!")

    override suspend fun sendIntegerAutocomplete(list: List<Choice.IntegerChoice>): Nothing =
        error("Can't send a autocomplete request via the HttpRequestManager!")

    override suspend fun sendNumberAutocomplete(list: List<Choice.NumberChoice>): Nothing =
        error("Can't send a autocomplete request via the HttpRequestManager!")

    override suspend fun sendModal(title: String, customId: String, builder: ModalBuilder.() -> Unit): Nothing =
        error("Can't send a form to a interaction that was already deferred!")
}