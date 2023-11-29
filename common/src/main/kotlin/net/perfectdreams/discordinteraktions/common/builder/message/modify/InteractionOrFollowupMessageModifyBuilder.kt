package net.perfectdreams.discordinteraktions.common.builder.message.modify

import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.NamedFile
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.AttachmentBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

// From Kord, however this is an interaction OR followup modify builder
class InteractionOrFollowupMessageModifyBuilder : MessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    var state = MessageModifyStateHolder()

    override var files: MutableList<NamedFile>? by state::files.delegate()

    override var attachments: MutableList<AttachmentBuilder>? by state::attachments.delegate()

    override var content: String? by state::content.delegate()

    override var embeds: MutableList<EmbedBuilder>? by state::embeds.delegate()

    override var allowedMentions: AllowedMentionsBuilder? by state::allowedMentions.delegate()

    override var components: MutableList<MessageComponentBuilder>? by state::components.delegate()

    override fun toFollowupMessageModifyBuilder(): FollowupMessageModifyBuilder {
        return FollowupMessageModifyBuilder().apply {
            runIfNotMissing(state.content) { this.content = it }
            runIfNotMissing(state.allowedMentions) { this.allowedMentions = it }
            runIfNotMissing(state.components) { this.components = it }
            runIfNotMissing(state.embeds) { this.embeds = it }
            runIfNotMissing(state.flags) { this.flags = it }
            runIfNotMissing(state.attachments) { this.attachments = it }
            // see https://github.com/kordlib/kord/commit/b6e878afe3a50d8251480febaf5e10fe6557cebd#diff-f2320acb4cd4c98c2766920d3a3693116b681c36d6ee373cc3f16a2065ca98ccR86
            runIfNotMissing(state.files) {
                this.files.clear()
                this.files.addAll(it.orEmpty())
            }
        }
    }

    override fun toInteractionMessageResponseModifyBuilder(): InteractionResponseModifyBuilder {
        return InteractionResponseModifyBuilder().apply {
            runIfNotMissing(state.content) { this.content = it }
            runIfNotMissing(state.allowedMentions) { this.allowedMentions = it }
            runIfNotMissing(state.components) { this.components = it }
            runIfNotMissing(state.embeds) { this.embeds = it }
            runIfNotMissing(state.flags) { this.flags = it }
            runIfNotMissing(state.attachments) { this.attachments = it }
            // see https://github.com/kordlib/kord/commit/b6e878afe3a50d8251480febaf5e10fe6557cebd#diff-f2320acb4cd4c98c2766920d3a3693116b681c36d6ee373cc3f16a2065ca98ccR86
            runIfNotMissing(state.files) {
                this.files.clear()
                this.files.addAll(it.orEmpty())
            }
        }
    }
}