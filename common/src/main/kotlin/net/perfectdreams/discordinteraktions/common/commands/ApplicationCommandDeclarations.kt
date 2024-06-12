package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.Locale
import dev.kord.common.entity.Permissions

/**
 * Base class of every application declaration, because all interactions share a [name]
 */
public sealed class ApplicationCommandDeclaration {
    public abstract val name: String
    public abstract val nameLocalizations: Map<Locale, String>?
}

public abstract class SlashCommandDeclaration : ApplicationCommandDeclaration() {
    public abstract val description: String
    public abstract val descriptionLocalizations: Map<Locale, String>?
    public abstract val executor: SlashCommandExecutor?
    public abstract val defaultMemberPermissions: Permissions?
    public abstract val dmPermission: Boolean?
    public abstract val subcommands: List<SlashCommandDeclaration>
    public abstract val subcommandGroups: List<SlashCommandGroupDeclaration>
    public abstract val nsfw: Boolean?
}

public abstract class SlashCommandGroupDeclaration : ApplicationCommandDeclaration() {
    public abstract val description: String
    public abstract val descriptionLocalizations: Map<Locale, String>?
    public abstract val subcommands: List<SlashCommandDeclaration>
    public abstract val nsfw: Boolean?
}

public abstract class UserCommandDeclaration : ApplicationCommandDeclaration() {
    public abstract val defaultMemberPermissions: Permissions?
    public abstract val executor: UserCommandExecutor // User/Message commands always requires an executor, that's why it is not nullable!
    public abstract val dmPermission: Boolean?
    public abstract val nsfw: Boolean?
}

public abstract class MessageCommandDeclaration : ApplicationCommandDeclaration() {
    public abstract val defaultMemberPermissions: Permissions?
    public abstract val executor: MessageCommandExecutor // User/Message commands always requires an executor, that's why it is not nullable!
    public abstract val dmPermission: Boolean?
    public abstract val nsfw: Boolean?
}

// ===[ DEFAULT IMPLEMENTATIONS ]===
public class InteraKTionsSlashCommandDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val description: String,
    override val descriptionLocalizations: Map<Locale, String>? = null,
    override val executor: SlashCommandExecutor? = null,
    override val defaultMemberPermissions: Permissions?,
    override val dmPermission: Boolean?,
    override val subcommands: List<SlashCommandDeclaration>,
    override val subcommandGroups: List<SlashCommandGroupDeclaration>,
    override val nsfw: Boolean?
) : SlashCommandDeclaration()

public class InteraKTionsSlashCommandGroupDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val description: String,
    override val descriptionLocalizations: Map<Locale, String>? = null,
    override val subcommands: List<SlashCommandDeclaration>,
    override val nsfw: Boolean?
) : SlashCommandGroupDeclaration()

public class InteraKTionsUserCommandDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val executor: UserCommandExecutor,
    override val defaultMemberPermissions: Permissions?,
    override val dmPermission: Boolean?,
    override val nsfw: Boolean?
) : UserCommandDeclaration()

public class InteraKTionsMessageCommandDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val executor: MessageCommandExecutor,
    override val defaultMemberPermissions: Permissions?,
    override val dmPermission: Boolean?,
    override val nsfw: Boolean?
) : MessageCommandDeclaration()