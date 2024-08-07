package live.shuuyu.discordinteraktions.common.builder.message

import dev.kord.rest.NamedFile
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import io.ktor.client.request.forms.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Base message builder, used to share common properties and extension methods between create and modify builders.
 */
public interface MessageBuilder {
    /**
     * The text content of the message.
     */
    public var content: String?

    /**
     * The embedded content of the message.
     */
    public var embeds: MutableList<EmbedBuilder>?

    /**
     * The mentions in this message that are allowed to raise a notification.
     * Setting this to null will default to creating notifications for all mentions.
     */
    public var allowedMentions: AllowedMentionsBuilder?

    /**
     * The message components to include in this message.
     */
    public var components: MutableList<MessageComponentBuilder>?

    /**
     * The files to include as attachments.
     */
    public var files: MutableList<NamedFile>?

    /**
     * Adds a file with the [name] and [content] to the attachments.
     */
    public fun addFile(name: String, content: InputStream) {
        val files = this.files ?: mutableListOf()
        files += NamedFile(name, ChannelProvider { content.toByteReadChannel() })
        this.files = files
    }

    /**
     * Adds a file with the given [path] to the attachments.
     */
    public suspend fun addFile(path: Path): Unit = withContext(Dispatchers.IO) {
        addFile(path.fileName.toString(), Files.newInputStream(path))
    }
}

/**
 * Creates an embed using Kord's [EmbedBuilder].
 */
@OptIn(ExperimentalContracts::class)
public inline fun MessageBuilder.embed(block: EmbedBuilder.() -> (Unit)) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    embeds = (embeds ?: mutableListOf()).also {
        it.add(EmbedBuilder().apply(block))
    }
}

/**
 * Configures the mentions that should trigger a ping. Not calling this function will result in the default behavior
 * (ping everything), calling this function but not configuring it before the request is build will result in all
 * pings being ignored.
 */
@OptIn(ExperimentalContracts::class)
public inline fun MessageBuilder.allowedMentions(block: AllowedMentionsBuilder.() -> (Unit) = {}) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    allowedMentions = (allowedMentions ?: AllowedMentionsBuilder()).apply(block)
}


@OptIn(ExperimentalContracts::class)
public inline fun MessageBuilder.actionRow(builder: ActionRowBuilder.() -> (Unit)) {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    components = (components ?: mutableListOf()).also {
        it.add(ActionRowBuilder().apply(builder))
    }
}