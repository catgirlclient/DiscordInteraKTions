package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

/**
 * This is the class that should be inherited if you
 * want to create an Application Command.
 */
public sealed class ApplicationCommandExecutor {
    /**
     * Used by the [ApplicationCommandDeclaration] to match declarations to executors.
     *
     * By default, the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    public open fun signature(): Any = this::class
}

/**
 * Creates the executor for a [slashCommand]. To declare it, you'll want to use [SlashCommandDeclarationWrapper].
 *
 * @see SlashCommandDeclarationWrapper
 * @see ApplicationCommandOptions
 */
public abstract class SlashCommandExecutor : ApplicationCommandExecutor() {
    public open val options: ApplicationCommandOptions = ApplicationCommandOptions.NO_OPTIONS

    public abstract suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments)
}

/**
 * Creates the executor for a [userCommand]. To declare it, you'll want to use [UserCommandDeclarationWrapper]
 *
 * @see UserCommandDeclarationWrapper
 */
public abstract class UserCommandExecutor : ApplicationCommandExecutor() {
    public abstract suspend fun execute(context: ApplicationCommandContext, targetUser: User, targetMember: Member?)
}

/**
 * This is the class that should be inherited if you
 * want to create an Message Command.
 */
public abstract class MessageCommandExecutor : ApplicationCommandExecutor() {
    public abstract suspend fun execute(context: ApplicationCommandContext, targetMessage: Message)
}