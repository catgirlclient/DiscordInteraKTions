package net.perfectdreams.discordinteraktions.common.builder.message.create

import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder
import dev.kord.rest.builder.message.create.InteractionResponseCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.MessageBuilder

/**
 * The base builder for creating a new message.
 */
// From Kord
public sealed interface MessageCreateBuilder : MessageBuilder {
    /**
     * Whether this message should be played as a text-to-speech message.
     */
    public var tts: Boolean?

    public fun toFollowupMessageCreateBuilder(): FollowupMessageCreateBuilder
    public fun toInteractionMessageResponseCreateBuilder(): InteractionResponseCreateBuilder
}
