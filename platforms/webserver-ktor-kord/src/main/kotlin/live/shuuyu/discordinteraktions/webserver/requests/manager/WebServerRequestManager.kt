package live.shuuyu.discordinteraktions.webserver.requests.manager

import dev.kord.common.entity.*
import dev.kord.common.entity.optional.*
import dev.kord.core.Kord
import dev.kord.rest.builder.interaction.ModalBuilder
import dev.kord.rest.json.request.AutoCompleteResponseCreateRequest
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.json.request.ModalResponseCreateRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import live.shuuyu.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import live.shuuyu.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import live.shuuyu.discordinteraktions.common.entities.messages.EditableMessage
import live.shuuyu.discordinteraktions.common.requests.InteractionRequestState
import live.shuuyu.discordinteraktions.common.requests.RequestBridge
import live.shuuyu.discordinteraktions.common.requests.managers.HttpRequestManager
import live.shuuyu.discordinteraktions.common.requests.managers.RequestManager
import live.shuuyu.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionEphemeralMessage
import live.shuuyu.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionPublicMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param kord The [Kord] instance
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 * @param call The request data
 */
public class WebServerRequestManager(
    bridge: RequestBridge,
    public val kord: Kord,
    public val applicationId: Snowflake,
    public val interactionToken: String,
    public val call: ApplicationCall,
) : RequestManager(bridge) {
    public companion object {
        private val logger = KotlinLogging.logger {}
    }

    init {
        require(bridge.state.value == InteractionRequestState.NOT_REPLIED_YET) {
            "HttpRequestManager should be in the NOT_REPLIED_YET state!"
        }
    }

    override suspend fun deferChannelMessage() {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredChannelMessageWithSource.type)
            }.toString(),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            kord,
            applicationId,
            interactionToken
        )
    }

    override suspend fun deferChannelMessageEphemerally() {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredChannelMessageWithSource.type)

                putJsonObject("data") {
                    put("flags", 64)
                }
            }.toString(),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            kord,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        call.respondText(
            Json.encodeToString(
                InteractionResponseCreateRequest(
                    type = InteractionResponseType.ChannelMessageWithSource,
                    data = Optional(
                        InteractionApplicationCommandCallbackData(
                            content = Optional(message.content).coerceToMissing(),
                            tts = Optional(message.tts).coerceToMissing().toPrimitive(),
                            embeds = message.embeds?.map { it.toRequest() }.optional().coerceToMissing(),
                            allowedMentions = Optional(message.allowedMentions).coerceToMissing().map { it.build() },
                            components = message.components?.map { it.build() }.optional().coerceToMissing(),
                            flags = MessageFlags {}.optional()
                        )
                    )
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            kord,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionPublicMessage(
            kord,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        call.respondText(
            Json.encodeToString(
                InteractionResponseCreateRequest(
                    type = InteractionResponseType.ChannelMessageWithSource,
                    data = Optional(
                        InteractionApplicationCommandCallbackData(
                            content = Optional(message.content).coerceToMissing(),
                            tts = Optional(message.tts).coerceToMissing().toPrimitive(),
                            embeds = message.embeds?.map { it.toRequest() }.optional().coerceToMissing(),
                            allowedMentions = Optional(message.allowedMentions).coerceToMissing().map { it.build() },
                            components = message.components?.map { it.build() }.optional().coerceToMissing(),
                            flags = MessageFlags {
                                +MessageFlag.Ephemeral
                            }.optional()
                        )
                    )
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            kord,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionEphemeralMessage(
            kord,
            applicationId,
            interactionToken
        )
    }

    override suspend fun deferUpdateMessage() {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredUpdateMessage.type)
            }.toString(),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.DEFERRED_UPDATE_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            kord,
            applicationId,
            interactionToken
        )
    }

    override suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        call.respondText(
            Json.encodeToString(
                InteractionResponseCreateRequest(
                    type = InteractionResponseType.UpdateMessage,
                    data = Optional(
                        InteractionApplicationCommandCallbackData(
                            content = Optional(message.content).coerceToMissing(),
                            embeds = message.embeds?.map { it.toRequest() }.optional().coerceToMissing(),
                            allowedMentions = Optional(message.allowedMentions?.build()).coerceToMissing(),
                            components = message.components?.map { it.build() }.optional().coerceToMissing(),
                            flags = MessageFlags {}.optional()
                        )
                    )
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            kord,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionPublicMessage(
            kord,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendStringAutocomplete(list: List<Choice.StringChoice>) {
        call.respondText(
            Json.encodeToString(
                AutoCompleteResponseCreateRequest(
                    InteractionResponseType.ApplicationCommandAutoCompleteResult,
                    DiscordAutoComplete(list)
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }

    override suspend fun sendIntegerAutocomplete(list: List<Choice.IntegerChoice>) {
        call.respondText(
            Json.encodeToString(
                AutoCompleteResponseCreateRequest(
                    InteractionResponseType.ApplicationCommandAutoCompleteResult,
                    DiscordAutoComplete(list)
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }

    override suspend fun sendNumberAutocomplete(list: List<Choice.NumberChoice>) {
        call.respondText(
            Json.encodeToString(
                AutoCompleteResponseCreateRequest(
                    InteractionResponseType.ApplicationCommandAutoCompleteResult,
                    DiscordAutoComplete(list)
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }

    override suspend fun sendModal(title: String, customId: String, builder: ModalBuilder.() -> Unit) {
        call.respondText(
            Json.encodeToString(
                ModalResponseCreateRequest(
                    InteractionResponseType.Modal,
                    ModalBuilder(title, customId).apply(builder).toRequest()
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }
}
