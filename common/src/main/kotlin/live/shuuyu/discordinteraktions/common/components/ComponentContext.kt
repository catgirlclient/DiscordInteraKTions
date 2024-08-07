package live.shuuyu.discordinteraktions.common.components

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import live.shuuyu.discordinteraktions.common.InteractionContext
import live.shuuyu.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import live.shuuyu.discordinteraktions.common.entities.messages.EditableMessage
import live.shuuyu.discordinteraktions.common.entities.messages.Message
import live.shuuyu.discordinteraktions.common.interactions.InteractionData
import live.shuuyu.discordinteraktions.common.requests.RequestBridge

public open class ComponentContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    public val componentExecutorDeclaration: ComponentExecutorDeclaration,
    public val message: Message,
    public val dataOrNull: String?,
    interactionData: InteractionData,
    discordInteractionData: DiscordInteraction
) : InteractionContext(bridge, sender, channelId, interactionData, discordInteractionData) {
    public val data: String
        get() = dataOrNull ?: error("There isn't any custom data present in this component context!")

    public suspend fun deferUpdateMessage() {
        if (!isDeferred) {
            bridge.manager.deferUpdateMessage()
        }
    }

    public suspend inline fun updateMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit)): EditableMessage =
        updateMessage(InteractionOrFollowupMessageModifyBuilder().apply(block))

    public suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        // Check if state matches what we expect
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow-up message
            deferUpdateMessage()
        }

        return bridge.manager.updateMessage(message)
    }
}