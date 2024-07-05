package live.shuuyu.discordinteraktions.common.entities.messages

import live.shuuyu.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder

public interface EditableMessage {
    public suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage
}

// This isn't in the interface because we want this to be inline, allowing users to use suspendable functions within the builder
public suspend inline fun EditableMessage.editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit)): EditableMessage =
    editMessage(InteractionOrFollowupMessageModifyBuilder().apply(block))