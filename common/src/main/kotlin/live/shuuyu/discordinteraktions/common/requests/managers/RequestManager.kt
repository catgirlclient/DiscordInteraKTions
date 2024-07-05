package live.shuuyu.discordinteraktions.common.requests.managers

import dev.kord.common.entity.Choice
import dev.kord.rest.builder.interaction.ModalBuilder
import live.shuuyu.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import live.shuuyu.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import live.shuuyu.discordinteraktions.common.entities.messages.EditableMessage
import live.shuuyu.discordinteraktions.common.requests.RequestBridge

public abstract class RequestManager(public val bridge: RequestBridge) {
    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    public abstract suspend fun deferChannelMessage()

    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    public abstract suspend fun deferChannelMessageEphemerally()

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    public abstract suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    public abstract suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage

    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will not see a loading status for the interaction.
     */
    public abstract suspend fun deferUpdateMessage()

    /**
     * The usual way of editing a message to a specific channel/user.
     */
    public abstract suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage

    public abstract suspend fun sendStringAutocomplete(list: List<Choice.StringChoice>)

    public abstract suspend fun sendIntegerAutocomplete(list: List<Choice.IntegerChoice>)

    public abstract suspend fun sendNumberAutocomplete(list: List<Choice.NumberChoice>)

    public abstract suspend fun sendModal(title: String, customId: String, builder: ModalBuilder.() -> Unit)
}