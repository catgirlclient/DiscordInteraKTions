package live.shuuyu.discordinteraktions.common.commands.options

import dev.kord.common.entity.DiscordAttachment
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel

public open class ApplicationCommandOptions {
    public companion object {
        public val NO_OPTIONS: ApplicationCommandOptions = object : ApplicationCommandOptions() {}
    }

    public val registeredOptions: MutableList<InteraKTionsCommandOption<*>> = mutableListOf()
    public val references: MutableList<OptionReference<*>> = mutableListOf()

    public fun string(
        name: String,
        description: String,
        builder: StringCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<String> = StringCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalString(
        name: String,
        description: String,
        builder: NullableStringCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<String?> = NullableStringCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun integer(
        name: String,
        description: String,
        builder: IntegerCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Long> = IntegerCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalInteger(
        name: String,
        description: String,
        builder: NullableIntegerCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Long?> = NullableIntegerCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun number(
        name: String,
        description: String,
        builder: NumberCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Double> = NumberCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalNumber(
        name: String,
        description: String,
        builder: NullableNumberCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Double?> = NullableNumberCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun boolean(
        name: String,
        description: String,
        builder: BooleanCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Boolean> = BooleanCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalBoolean(
        name: String,
        description: String,
        builder: NullableBooleanCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Boolean?> = NullableBooleanCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun user(
        name: String,
        description: String,
        builder: UserCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<User> = UserCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalUser(
        name: String,
        description: String,
        builder: NullableUserCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<User?> = NullableUserCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun role(
        name: String,
        description: String,
        builder: RoleCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Role> = RoleCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalRole(
        name: String,
        description: String,
        builder: NullableRoleCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Role?> = NullableRoleCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun channel(
        name: String,
        description: String,
        builder: ChannelCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Channel> = ChannelCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalChannel(
        name: String,
        description: String,
        builder: NullableChannelCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Channel?> = NullableChannelCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun mentionable(
        name: String,
        description: String,
        builder: MentionableCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Any> = MentionableCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalMentionable(
        name: String,
        description: String,
        builder: NullableMentionableCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<Any?> = NullableMentionableCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun attachment(
        name: String,
        description: String,
        builder: AttachmentCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<DiscordAttachment> = AttachmentCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    public fun optionalAttachment(
        name: String,
        description: String,
        builder: NullableAttachmentCommandOptionBuilder.() -> (Unit) = {}
    ): OptionReference<DiscordAttachment?> = NullableAttachmentCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }
}

/**
 * Registers a [optionBuilder] to an [ApplicationCommandOptions]
 *
 * @param optionBuilder the option builder
 * @return an [OptionReference]
 */
public inline fun <reified T, ChoiceableType> ApplicationCommandOptions.register(
    optionBuilder: CommandOptionBuilder<T, ChoiceableType>
): OptionReference<T> {
    if (registeredOptions.any { it.name == optionBuilder.name })
        throw IllegalArgumentException("Duplicate argument \"${optionBuilder.name}\"!")

    val optionReference = OptionReference<T>(optionBuilder.name, optionBuilder.required)

    registeredOptions.add(optionBuilder.build())
    references.add(optionReference)

    return optionReference
}