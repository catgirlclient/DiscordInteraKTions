package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.Locale
import dev.kord.common.entity.Permissions
import net.perfectdreams.discordinteraktions.common.annotations.InteraKTionsDsl

// ===[ SLASH COMMANDS ]===
public fun slashCommand(
    name: String,
    description: String,
    block: SlashCommandDeclarationBuilder.() -> (Unit)
): SlashCommandDeclarationBuilder = SlashCommandDeclarationBuilder(name, description).apply(block)

@InteraKTionsDsl
public class SlashCommandDeclarationBuilder(
    public val name: String,
    public val description: String
) {
    public var nameLocalizations: Map<Locale, String>? = null
    public var descriptionLocalizations: Map<Locale, String>? = null
    public var executor: SlashCommandExecutor? = null
    public val subcommands: MutableList<SlashCommandDeclarationBuilder> = mutableListOf()
    public val subcommandGroups: MutableList<SlashCommandGroupDeclarationBuilder> = mutableListOf()
    // Only root commands can have permissions and dmPermission
    public var defaultMemberPermissions: Permissions? = null
    public var dmPermission: Boolean? = null
    public var nsfw: Boolean? = null

    public fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
    }

    public fun subcommandGroup(name: String, description: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += SlashCommandGroupDeclarationBuilder(name, description).apply(block)
    }

    public fun build(): SlashCommandDeclaration {
        return InteraKTionsSlashCommandDeclaration(
            name,
            nameLocalizations,
            description,
            descriptionLocalizations,
            executor,
            defaultMemberPermissions,
            dmPermission,
            subcommands.map { it.build() },
            subcommandGroups.map { it.build() },
            nsfw
        )
    }
}

@InteraKTionsDsl
public class SlashCommandGroupDeclarationBuilder(
    public val name: String,
    public val description: String
) {
    public var nameLocalizations: Map<Locale, String>? = null
    public var descriptionLocalizations: Map<Locale, String>? = null
    // Groups can't have executors!
    public val subcommands: MutableList<SlashCommandDeclarationBuilder> = mutableListOf()
    public var nsfw: Boolean? = null

    public fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
    }

    public fun build(): SlashCommandGroupDeclaration {
        return InteraKTionsSlashCommandGroupDeclaration(
            name,
            nameLocalizations,
            description,
            descriptionLocalizations,
            subcommands.map { it.build() },
            nsfw
        )
    }
}

// ===[ USER COMMANDS ]===
public fun userCommand(
    name: String,
    executor: UserCommandExecutor
): UserCommandDeclarationBuilder = UserCommandDeclarationBuilder(name, executor)

@InteraKTionsDsl
public class UserCommandDeclarationBuilder(public val name: String, public val executor: UserCommandExecutor) {
    public var nameLocalizations: Map<Locale, String>? = null
    public var defaultMemberPermissions: Permissions? = null
    public var dmPermission: Boolean? = null
    public var nsfw: Boolean? = null

    public fun build(): UserCommandDeclaration {
        return InteraKTionsUserCommandDeclaration(
            name,
            nameLocalizations,
            executor,
            defaultMemberPermissions,
            dmPermission,
            nsfw
        )
    }
}

// ===[ MESSAGE COMMANDS ]===
public fun messageCommand(
    name: String,
    executor: MessageCommandExecutor
): MessageCommandDeclarationBuilder = MessageCommandDeclarationBuilder(name, executor)

@InteraKTionsDsl
public class MessageCommandDeclarationBuilder(public val name: String, public val executor: MessageCommandExecutor) {
    public var nameLocalizations: Map<Locale, String>? = null
    public var defaultMemberPermissions: Permissions? = null
    public var dmPermission: Boolean? = null
    public var nsfw: Boolean? = null

    public fun build(): MessageCommandDeclaration {
        return InteraKTionsMessageCommandDeclaration(
            name,
            nameLocalizations,
            executor,
            defaultMemberPermissions,
            dmPermission,
            nsfw
        )
    }
}