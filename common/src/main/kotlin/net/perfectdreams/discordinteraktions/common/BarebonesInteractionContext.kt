package net.perfectdreams.discordinteraktions.common

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.builder.interaction.ModalBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.modals.ModalExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.requests.managers.HttpRequestManager
import net.perfectdreams.discordinteraktions.common.utils.Observable

/**
 * This is a "barebones" implementation of a [InteractionContext], where only the essential constructor parameters are present.
 *
 * This is useful if you are trying to reply to an interaction where you only have its essential information (like the interaction token).
 */
public open class BarebonesInteractionContext(
    public var bridge: RequestBridge
) {
    public val isDeferred: Boolean
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET
    public var wasInitiallyDeferredEphemerally: Boolean = false

    /**
     * Defers the application command request message with a public message
     */
    public suspend fun deferChannelMessage() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessage()
        wasInitiallyDeferredEphemerally = false
    }

    /**
     * Defers the application command request message with a ephemeral message
     */
    public suspend fun deferChannelMessageEphemerally() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessageEphemerally()
        wasInitiallyDeferredEphemerally = true
    }

    public suspend inline fun sendMessage(block: InteractionOrFollowupMessageCreateBuilder.() -> (Unit)): EditableMessage =
        sendPublicMessage(InteractionOrFollowupMessageCreateBuilder(false).apply(block))

    public suspend inline fun sendEphemeralMessage(block: InteractionOrFollowupMessageCreateBuilder.() -> (Unit)): EditableMessage =
        sendEphemeralMessage(InteractionOrFollowupMessageCreateBuilder(true).apply(block))

    public suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (wasInitiallyDeferredEphemerally)
                error("Trying to send a public message but the message was originally deferred ephemerally! Change the \"deferMessage(...)\" call to be public")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferChannelMessage()
        }

        return bridge.manager.sendPublicMessage(message)
    }

    public suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (!wasInitiallyDeferredEphemerally)
                error("Trying to send a ephemeral message but the message was originally deferred as public! Change the \"deferMessage(...)\" call to be ephemeral")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferChannelMessage()
        }

        return bridge.manager.sendEphemeralMessage(message)
    }

    public suspend fun sendModal(
        declaration: ModalExecutorDeclaration,
        title: String,
        builder: ModalBuilder.() -> (Unit)
    ): Unit = sendModal(declaration.id, title, builder)

    public suspend fun sendModal(
        declaration: ModalExecutorDeclaration,
        data: String,
        title: String,
        builder: ModalBuilder.() -> (Unit)
    ): Unit = sendModal(declaration.id, data, title, builder)

    public suspend fun sendModal(
        id: String,
        data: String,
        title: String,
        builder: ModalBuilder.() -> (Unit)
    ): Unit = sendModal("$id:$data", title, builder)

    public suspend fun sendModal(idWithData: String, title: String, builder: ModalBuilder.() -> (Unit)) {
        return bridge.manager.sendModal(title, idWithData, builder)
    }
}

/**
 * Creates a [BarebonesInteractionContext] with the [rest], [applicationId], [interactionToken] and [requestState].
 *
 * This is useful if you are trying to reply to an interaction where you only have its essential information (like the interaction token).
 */
public fun BarebonesInteractionContext(
    kord: Kord,
    applicationId: Snowflake,
    interactionToken: String,
    requestState: InteractionRequestState = InteractionRequestState.ALREADY_REPLIED
): BarebonesInteractionContext {
    val bridge = RequestBridge(Observable(requestState))

    bridge.manager = HttpRequestManager(
        bridge,
        kord,
        applicationId,
        interactionToken
    )

    return BarebonesInteractionContext(bridge)
}