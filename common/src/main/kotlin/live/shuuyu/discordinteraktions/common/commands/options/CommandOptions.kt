package live.shuuyu.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.*
import dev.kord.common.entity.optional.optional
import dev.kord.core.Kord
import dev.kord.core.cache.data.ChannelData
import dev.kord.core.cache.data.MemberData
import dev.kord.core.cache.data.RoleData
import dev.kord.core.cache.data.UserData
import dev.kord.core.entity.Member
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel
import dev.kord.rest.builder.interaction.*
import live.shuuyu.discordinteraktions.common.autocomplete.AutocompleteHandler

public interface InteraKTionsCommandOption<T> {
    public val name: String

    public fun register(builder: BaseInputChatBuilder)
    public fun parse(kord: Kord, args: List<CommandArgument<*>>, interaction: DiscordInteraction): T?
}

public interface NameableCommandOption<T> : InteraKTionsCommandOption<T> {
    public val description: String
    public val nameLocalizations: Map<Locale, String>?
    public val descriptionLocalizations: Map<Locale, String>?
}

public interface DiscordCommandOption<T> : NameableCommandOption<T> {
    public val required: Boolean
}

public interface GenericCommandOption<T> : DiscordCommandOption<T> {
    override fun parse(kord: Kord, args: List<CommandArgument<*>>, interaction: DiscordInteraction): T? {
        return args.firstOrNull { it.name == name }?.value as T
    }
}

public interface ChoiceableCommandOption<T> {
    public val choices: List<CommandChoice<T>>?
    public val autocompleteExecutor: AutocompleteHandler<T>?
}

// ===[ STRING ]===
public interface StringCommandOption : GenericCommandOption<String>, ChoiceableCommandOption<String> {
    public val minLength: Int?
    public val maxLength: Int?

    override fun register(builder: BaseInputChatBuilder) {
        builder.string(this@StringCommandOption.name, this@StringCommandOption.description) {
            this.nameLocalizations = this@StringCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@StringCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@StringCommandOption.required
            this.autocomplete = this@StringCommandOption.autocompleteExecutor != null
            this.minLength = this@StringCommandOption.minLength
            this.maxLength = this@StringCommandOption.maxLength

            this@StringCommandOption.choices?.forEach { choice ->
                choice(choice.name, choice.value, choice.nameLocalizations.optional())
            }
        }
    }
}

public data class DefaultStringCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean,
    override val choices: List<CommandChoice<String>>?,
    override val minLength: Int?,
    override val maxLength: Int?,
    override val autocompleteExecutor: AutocompleteHandler<String>?
) : StringCommandOption

// ===[ INTEGER ]===
public interface IntegerCommandOption : GenericCommandOption<Long>, ChoiceableCommandOption<Long> {
    public val minValue: Long?
    public val maxValue: Long?

    override fun register(builder: BaseInputChatBuilder) {
        builder.integer(this@IntegerCommandOption.name, this@IntegerCommandOption.description) {
            this.nameLocalizations = this@IntegerCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@IntegerCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@IntegerCommandOption.required
            this.autocomplete = this@IntegerCommandOption.autocompleteExecutor != null
            this.minValue = this@IntegerCommandOption.minValue
            this.maxValue = this@IntegerCommandOption.maxValue

            this@IntegerCommandOption.choices?.forEach { choice ->
                choice(choice.name, choice.value, choice.nameLocalizations.optional())
            }
        }
    }
}

public data class DefaultIntegerCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean,
    override val choices: List<CommandChoice<Long>>?,
    override val minValue: Long?,
    override val maxValue: Long?,
    override val autocompleteExecutor: AutocompleteHandler<Long>?
) : IntegerCommandOption

// ===[ NUMBER ]===
public interface NumberCommandOption : GenericCommandOption<Double>, ChoiceableCommandOption<Double> {
    public val minValue: Double?
    public val maxValue: Double?

    override fun register(builder: BaseInputChatBuilder) {
        builder.number(this@NumberCommandOption.name, this@NumberCommandOption.description) {
            this.nameLocalizations = this@NumberCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@NumberCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@NumberCommandOption.required
            this.autocomplete = this@NumberCommandOption.autocompleteExecutor != null
            this.minValue = this@NumberCommandOption.minValue
            this.maxValue = this@NumberCommandOption.maxValue

            this@NumberCommandOption.choices?.forEach { choice ->
                choice(choice.name, choice.value, choice.nameLocalizations.optional())
            }
        }
    }
}

public data class DefaultNumberCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean,
    override val choices: List<CommandChoice<Double>>?,
    override val minValue: Double?,
    override val maxValue: Double?,
    override val autocompleteExecutor: AutocompleteHandler<Double>?
) : NumberCommandOption

// ===[ BOOLEAN ]===
public interface BooleanCommandOption : GenericCommandOption<Boolean> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.boolean(this@BooleanCommandOption.name, this@BooleanCommandOption.description) {
            this.nameLocalizations = this@BooleanCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@BooleanCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@BooleanCommandOption.required
        }
    }
}

public data class DefaultBooleanCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean
) : BooleanCommandOption

// ===[ USER ]===
public interface UserCommandOption : DiscordCommandOption<User> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.user(this@UserCommandOption.name, this@UserCommandOption.description) {
            this.nameLocalizations = this@UserCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@UserCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@UserCommandOption.required
        }
    }

    override fun parse(kord: Kord, args: List<CommandArgument<*>>, interaction: DiscordInteraction): User? {
        val userId = args.firstOrNull { it.name == name }?.value as Snowflake? ?: return null
        val resolvedMembers = interaction.data.resolved.value?.members?.value
        val resolvedUsers = interaction.data.resolved.value?.users?.value

        val guildId = interaction.guildId.value
        val resolvedMember = resolvedMembers?.get(userId)
        // If the user is null, then let's get out of here!
        val resolvedUser = resolvedUsers?.get(userId) ?: return null

        val userData = UserData.from(resolvedUser)

        // Maybe it is a member, hmmm :kurama_imitando_emojis:
        if (guildId != null && resolvedMember != null) {
            // If the guildId isn't null, and the resolvedMember is also not null, let's return a Member object instead!
            val memberData = MemberData.from(userId, guildId, resolvedMember)

            return Member(memberData, userData, kord)
        }

        // Sadly we don't have a member reference, so let's pass a User object instead...
        return User(userData, kord)
    }
}

public data class DefaultUserCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean
) : UserCommandOption

// ===[ ROLE ]===
public interface RoleCommandOption : DiscordCommandOption<Role> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.role(this@RoleCommandOption.name, this@RoleCommandOption.description) {
            this.nameLocalizations = this@RoleCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@RoleCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@RoleCommandOption.required
        }
    }

    override fun parse(kord: Kord, args: List<CommandArgument<*>>, interaction: DiscordInteraction): Role? {
        val roleId = args.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.roles?.value

        return resolved?.get(roleId)?.let {
            val guildId = interaction.guildId.value ?: error("Trying to parse a role reference on a interaction that didn't happen in a guild! Because we don't know the guild ID, we can't create a role ID instance!")
            Role(RoleData.from(guildId, it), kord)
        }
    }
}

public data class DefaultRoleCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean
) : RoleCommandOption

// ===[ CHANNEL ]===
public interface ChannelCommandOption : DiscordCommandOption<Channel> {
    public val channelTypes: List<ChannelType>?

    override fun register(builder: BaseInputChatBuilder) {
        builder.channel(this@ChannelCommandOption.name, this@ChannelCommandOption.description) {
            this.nameLocalizations = this@ChannelCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@ChannelCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@ChannelCommandOption.required
            this.channelTypes = this@ChannelCommandOption.channelTypes
        }
    }

    override fun parse(kord: Kord, args: List<CommandArgument<*>>, interaction: DiscordInteraction): Channel? {
        val channelId = args.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.channels?.value

        return resolved?.get(channelId)?.let {
            Channel.from(ChannelData.from(it), kord)
        }
    }
}

public data class DefaultChannelCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean,
    override val channelTypes: List<ChannelType>?
) : ChannelCommandOption

// ===[ MENTIONABLE ]===
public interface MentionableCommandOption : DiscordCommandOption<Any> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.mentionable(this@MentionableCommandOption.name, this@MentionableCommandOption.description) {
            this.nameLocalizations = this@MentionableCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@MentionableCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@MentionableCommandOption.required
        }
    }

    override fun parse(
        kord: Kord,
        args: List<CommandArgument<*>>,
        interaction: DiscordInteraction
    ): Any? {
        // Mentionable objects can be User OR Role
        val userId = args.firstOrNull { it.name == name }?.value as Snowflake?
        val resolvedUser = interaction.data.resolved.value?.users?.value

        val kordUser = resolvedUser?.get(userId)?.let { User(UserData.from(it), kord) }
        if (kordUser != null)
            return kordUser

        val roleId = args.firstOrNull { it.name == name }?.value as Snowflake?
        val resolvedRole = interaction.data.resolved.value?.roles?.value

        return resolvedRole?.get(roleId)?.let {
            val guildId = interaction.guildId.value ?: error("Trying to parse a role reference on a interaction that didn't happen in a guild! Because we don't know the guild ID, we can't create a role ID instance!")
            Role(RoleData.from(guildId, it), kord)
        }
    }
}

public data class DefaultMentionableCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean
) : MentionableCommandOption

// ===[ ATTACHMENT ]===
public interface AttachmentCommandOption : DiscordCommandOption<DiscordAttachment> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.attachment(this@AttachmentCommandOption.name, this@AttachmentCommandOption.description) {
            this.nameLocalizations = this@AttachmentCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@AttachmentCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@AttachmentCommandOption.required
        }
    }

    override fun parse(
        kord: Kord,
        args: List<CommandArgument<*>>,
        interaction: DiscordInteraction
    ): DiscordAttachment? {
        val attachmentId = args.firstOrNull { it.name == name }?.value as Snowflake?
        return interaction.data.resolved.value?.attachments?.value?.get(attachmentId)
    }
}

public data class DefaultAttachmentCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    override val required: Boolean
) : AttachmentCommandOption