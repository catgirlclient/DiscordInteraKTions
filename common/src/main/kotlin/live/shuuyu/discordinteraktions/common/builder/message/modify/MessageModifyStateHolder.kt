package live.shuuyu.discordinteraktions.common.builder.message.modify

import dev.kord.common.entity.MessageFlags
import dev.kord.common.entity.optional.Optional
import dev.kord.rest.NamedFile
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.AttachmentBuilder
import dev.kord.rest.builder.message.EmbedBuilder

/**
 * Utility container for message modify builder. This class contains
 * all possible fields as optionals.
 */
public class MessageModifyStateHolder {

    public var files: Optional<MutableList<NamedFile>> = Optional.Missing()

    public var content: Optional<String?> = Optional.Missing()

    public var embeds: Optional<MutableList<EmbedBuilder>> = Optional.Missing()

    public var flags: Optional<MessageFlags?> = Optional.Missing()

    public var allowedMentions: Optional<AllowedMentionsBuilder> = Optional.Missing()

    public var attachments: Optional<MutableList<AttachmentBuilder>> = Optional.Missing()

    public var components: Optional<MutableList<MessageComponentBuilder>> = Optional.Missing()
}