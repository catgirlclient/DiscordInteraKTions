package net.perfectdreams.discordinteraktions.common.builder.message.modify

import dev.kord.rest.builder.message.AttachmentBuilder
import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.MessageBuilder

public sealed interface MessageModifyBuilder : MessageBuilder {
    public var attachments: MutableList<AttachmentBuilder>?

    public fun toFollowupMessageModifyBuilder(): FollowupMessageModifyBuilder
    public fun toInteractionMessageResponseModifyBuilder(): InteractionResponseModifyBuilder
}