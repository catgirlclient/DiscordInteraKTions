package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordAttachment
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteHandler

public abstract class CommandOptionBuilder<T, ChoiceableType> {
    public abstract val name: String

    /**
     * If the command option is required in the command.
     *
     * If [required] is true and the argument is not present, the command will fail.
     */
    public abstract val required: Boolean

    public abstract fun build(): InteraKTionsCommandOption<ChoiceableType>
}

public abstract class DiscordCommandOptionBuilder<T, ChoiceableType> : CommandOptionBuilder<T, ChoiceableType>() {
    public abstract val description: String

    public var nameLocalizations: Map<Locale, String>? = null
    public var descriptionLocalizations: Map<Locale, String>? = null
}

public abstract class ChoiceableCommandOptionBuilder<T, ChoiceableType> : DiscordCommandOptionBuilder<T, ChoiceableType>() {
    public var choices: MutableList<CommandChoiceBuilder<ChoiceableType>>? = mutableListOf()
    public var autocompleteExecutor: AutocompleteHandler<ChoiceableType>? = null

    public fun choice(name: String, value: ChoiceableType, block: CommandChoiceBuilder<ChoiceableType>.() -> (Unit) = {}) {
        require(autocompleteExecutor == null) {
            "You can't use pre-defined choices with an autocomplete executor set!"
        }

        val builder = CommandChoiceBuilder(name, value).apply(block)

        if (choices == null)
            choices = mutableListOf()
        choices?.add(builder)
    }

    public fun autocomplete(handler: AutocompleteHandler<ChoiceableType>) {
        require(choices?.isNotEmpty() == false) {
            "You can't use autocomplete with pre-defined choices!"
        }

        autocompleteExecutor = handler
    }
}

// ===[ STRING ]===
public abstract class StringCommandOptionBuilderBase<T> : ChoiceableCommandOptionBuilder<T, String>() {
    public var minLength: Int? = null
    public var maxLength: Int? = null
    public var allowedLength: IntRange
        get() = error("This is a settable property only")
        set(value) {
            minLength = value.first
            maxLength = value.last
        }

    override fun build(): StringCommandOption = DefaultStringCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        choices?.map { it.build() },
        minLength,
        maxLength,
        autocompleteExecutor
    )
}

public class StringCommandOptionBuilder(
    override val name: String,
    override val description: String
) : StringCommandOptionBuilderBase<String>() {
    override val required: Boolean = true
}

public class NullableStringCommandOptionBuilder(
    override val name: String,
    override val description: String
) : StringCommandOptionBuilderBase<String?>() {
    override val required: Boolean = false
}

// ===[ INTEGER ]===
public abstract class IntegerCommandOptionBuilderBase<T> : ChoiceableCommandOptionBuilder<T, Long>() {
    public var minValue: Long? = null
    public var maxValue: Long? = null
    public var range: LongRange
        get() = error("This is a settable property only")
        set(value) {
            minValue = value.first
            maxValue = value.last
        }

    override fun build(): IntegerCommandOption = DefaultIntegerCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        choices?.map { it.build() },
        minValue,
        maxValue,
        autocompleteExecutor
    )
}

public class IntegerCommandOptionBuilder(
    override val name: String,
    override val description: String
) : IntegerCommandOptionBuilderBase<Long>() {
    override val required: Boolean = true
}

public class NullableIntegerCommandOptionBuilder(
    override val name: String,
    override val description: String
) : IntegerCommandOptionBuilderBase<Long?>() {
    override val required: Boolean = false
}

// ===[ NUMBER ]===
public abstract class NumberCommandOptionBuilderBase<T> : ChoiceableCommandOptionBuilder<T, Double>() {
    public var minValue: Double? = null
    public var maxValue: Double? = null
    public var range: ClosedFloatingPointRange<Double>
        get() = error("This is a settable property only")
        set(value) {
            minValue = value.start
            maxValue = value.endInclusive
        }

    override fun build(): NumberCommandOption = DefaultNumberCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        choices?.map { it.build() },
        minValue,
        maxValue,
        autocompleteExecutor
    )
}

public class NumberCommandOptionBuilder(
    override val name: String,
    override val description: String
) : NumberCommandOptionBuilderBase<Double>() {
    override val required: Boolean = true
}

public class NullableNumberCommandOptionBuilder(
    override val name: String,
    override val description: String
) : NumberCommandOptionBuilderBase<Double?>() {
    override val required: Boolean = false
}

// ===[ BOOLEAN ]===
public abstract class BooleanCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Boolean>() {
    override fun build(): BooleanCommandOption = DefaultBooleanCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

public class BooleanCommandOptionBuilder(
    override val name: String,
    override val description: String
) : BooleanCommandOptionBuilderBase<Boolean>() {
    override val required: Boolean = true
}

public class NullableBooleanCommandOptionBuilder(
    override val name: String,
    override val description: String
) : BooleanCommandOptionBuilderBase<Boolean?>() {
    override val required: Boolean = false
}

// ===[ USER ]===
public abstract class UserCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, User>() {
    override fun build(): UserCommandOption = DefaultUserCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

public class UserCommandOptionBuilder(
    override val name: String,
    override val description: String
) : UserCommandOptionBuilderBase<User>() {
    override val required: Boolean = true
}

public class NullableUserCommandOptionBuilder(
    override val name: String,
    override val description: String
) : UserCommandOptionBuilderBase<User?>() {
    override val required: Boolean = false
}

// ===[ ROLE ]===
public abstract class RoleCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Role>() {
    override fun build(): RoleCommandOption = DefaultRoleCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

public class RoleCommandOptionBuilder(
    override val name: String,
    override val description: String
) : RoleCommandOptionBuilderBase<Role>() {
    override val required: Boolean = true
}

public class NullableRoleCommandOptionBuilder(
    override val name: String,
    override val description: String
) : RoleCommandOptionBuilderBase<Role?>() {
    override val required: Boolean = false
}

// ===[ CHANNEL ]===
public abstract class ChannelCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Channel>() {
    public var channelTypes: List<ChannelType>? = null

    override fun build(): ChannelCommandOption = DefaultChannelCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        channelTypes
    )
}

public class ChannelCommandOptionBuilder(
    override val name: String,
    override val description: String
) : ChannelCommandOptionBuilderBase<Channel>() {
    override val required: Boolean = true
}

public class NullableChannelCommandOptionBuilder(
    override val name: String,
    override val description: String
) : ChannelCommandOptionBuilderBase<Channel?>() {
    override val required: Boolean = false
}

// ===[ MENTIONABLE ]===
public abstract class MentionableCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Any>() {
    override fun build(): MentionableCommandOption = DefaultMentionableCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

public class MentionableCommandOptionBuilder(
    override val name: String,
    override val description: String
) : MentionableCommandOptionBuilderBase<Any>() {
    override val required: Boolean = true
}

public class NullableMentionableCommandOptionBuilder(
    override val name: String,
    override val description: String
) : MentionableCommandOptionBuilderBase<Any?>() {
    override val required: Boolean = false
}

// ===[ ATTACHMENT ]===
public abstract class AttachmentCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, DiscordAttachment>() {
    override fun build(): AttachmentCommandOption = DefaultAttachmentCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

public class AttachmentCommandOptionBuilder(
    override val name: String,
    override val description: String
) : AttachmentCommandOptionBuilderBase<DiscordAttachment>() {
    override val required: Boolean = true
}

public class NullableAttachmentCommandOptionBuilder(
    override val name: String,
    override val description: String
) : AttachmentCommandOptionBuilderBase<DiscordAttachment?>() {
    override val required: Boolean = false
}