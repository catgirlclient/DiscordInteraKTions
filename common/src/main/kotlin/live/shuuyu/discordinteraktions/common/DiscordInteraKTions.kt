package live.shuuyu.discordinteraktions.common

import dev.kord.common.entity.DiscordApplicationCommand
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.builder.interaction.*
import dev.kord.rest.json.request.ApplicationCommandCreateRequest
import dev.kord.rest.service.RestClient
import live.shuuyu.discordinteraktions.common.commands.*
import live.shuuyu.discordinteraktions.common.commands.options.InteraKTionsCommandOption
import live.shuuyu.discordinteraktions.platforms.kord.utils.KordAutocompleteChecker
import live.shuuyu.discordinteraktions.platforms.kord.utils.KordCommandChecker
import live.shuuyu.discordinteraktions.platforms.kord.utils.KordComponentChecker
import live.shuuyu.discordinteraktions.platforms.kord.utils.KordModalChecker

public class DiscordInteraKTions(
    public val kord: Kord,
    public val applicationId: Snowflake,
) {
    public val rest: RestClient = kord.rest
    public val manager: InteractionsManager = InteractionsManager()

    public val autocompleteChecker: KordAutocompleteChecker = KordAutocompleteChecker(kord, manager)
    public val commandChecker: KordCommandChecker = KordCommandChecker(kord, manager)
    public val componentChecker: KordComponentChecker = KordComponentChecker(kord, manager)
    public val modalChecker: KordModalChecker = KordModalChecker(kord, manager)

    /**
     * Upserts all commands in the guild [guildId]
     */
    public suspend fun updateAllCommandsInGuild(
        guildId: Snowflake
    ): List<DiscordApplicationCommand> = rest.interaction.createGuildApplicationCommands(
        applicationId,
        guildId,
        createGuildApplicationCommandCreateRequests()
    )

    /**
     * Upserts all global commands
     */
    public suspend fun updateAllGlobalCommands(): List<DiscordApplicationCommand> =
        rest.interaction.createGlobalApplicationCommands(applicationId, createGlobalApplicationCommandCreateRequests())

    /**
     * Creates a list of guild [ApplicationCommandCreateRequest], which can be used with Kord to register the command on Discord.
     *
     * Because [ApplicationCommandCreateRequest] is [Serializable], you can create a hash of the requests and track if your bot needs to send an upsert request do Discord or not,
     * which can be useful to avoid ratelimits.
     */
    public fun createGuildApplicationCommandCreateRequests(): List<ApplicationCommandCreateRequest> =
        GuildMultiApplicationCommandBuilder().apply {
            manager.applicationCommandsDeclarations.forEach {
                convertCommandDeclarationToKord(this, it)
            }
        }.build()

    /**
     * Creates a list of global [ApplicationCommandCreateRequest], which can be used with Kord to register the command on Discord.
     *
     * Because [ApplicationCommandCreateRequest] is [Serializable], you can create a hash of the requests and track if your bot needs to send an upsert request do Discord or not,
     * which can be useful to avoid ratelimits.
     */
    public fun createGlobalApplicationCommandCreateRequests(): List<ApplicationCommandCreateRequest> =
        GlobalMultiApplicationCommandBuilder().apply {
            manager.applicationCommandsDeclarations.forEach {
                convertCommandDeclarationToKord(this, it)
            }
        }.build()

    private fun convertCommandDeclarationToKord(
        builder: MultiApplicationCommandBuilder,
        declaration: ApplicationCommandDeclaration
    ) {
        // Workaround because Kord's CommandCreateBuilder builders are internal now
        when (declaration) {
            is UserCommandDeclaration -> {
                return builder.user(declaration.name) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                    defaultMemberPermissions = declaration.defaultMemberPermissions
                    nsfw = declaration.nsfw

                    if (builder is GlobalMultiApplicationCommandBuilder)
                        (this as GlobalUserCommandCreateBuilder).dmPermission = declaration.dmPermission
                }
            }

            is MessageCommandDeclaration -> {
                return builder.message(declaration.name) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                    defaultMemberPermissions = declaration.defaultMemberPermissions
                    nsfw = declaration.nsfw

                    if (builder is GlobalMultiApplicationCommandBuilder)
                        (this as GlobalMessageCommandCreateBuilder).dmPermission = declaration.dmPermission
                }
            }

            is SlashCommandDeclaration -> {
                builder.input(declaration.name, declaration.description) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                    descriptionLocalizations = declaration.descriptionLocalizations?.toMutableMap()
                    defaultMemberPermissions = declaration.defaultMemberPermissions
                    nsfw = declaration.nsfw

                    if (builder is GlobalMultiApplicationCommandBuilder)
                        (this as GlobalChatInputCreateBuilder).dmPermission = declaration.dmPermission

                    options = mutableListOf() // Initialize an empty list so we can use it

                    // We can only have (subcommands OR subcommand groups) OR arguments
                    if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
                        declaration.subcommands.forEach {
                            options?.add(convertSubcommandDeclarationToKord(it))
                        }

                        declaration.subcommandGroups.forEach {
                            options?.add(convertSubcommandGroupDeclarationToKord(it))
                        }
                    } else {
                        val executor = declaration.executor

                        require(executor != null) { "Root command without a executor!" }

                        executor.options.registeredOptions.forEach {
                            convertCommandOptionToKord(it, this)
                        }
                    }
                }
            }

            is SlashCommandGroupDeclaration -> error("This should never be called because the convertCommandDeclarationToKord method is only called on a root!")
        }
    }

    private fun convertSubcommandDeclarationToKord(declaration: SlashCommandDeclaration): SubCommandBuilder {
        val commandData = SubCommandBuilder(declaration.name, declaration.description).apply {
            nameLocalizations = declaration.nameLocalizations?.toMutableMap()
            descriptionLocalizations = declaration.descriptionLocalizations?.toMutableMap()
            options = mutableListOf() // Initialize an empty list so we can use it
        }

        // This is a subcommand, so we SHOUlD have a non-null executor
        val executor = declaration.executor

        require(executor != null) { "Subcommand without a executor!" }

        executor.options.registeredOptions.forEach {
            convertCommandOptionToKord(it, commandData)
        }

        return commandData
    }

    private fun convertSubcommandGroupDeclarationToKord(declaration: SlashCommandGroupDeclaration): GroupCommandBuilder {
        val commandData = GroupCommandBuilder(declaration.name, declaration.description).apply {
            nameLocalizations = declaration.nameLocalizations?.toMutableMap()
            descriptionLocalizations = declaration.descriptionLocalizations?.toMutableMap()
            options = mutableListOf() // Initialize an empty list so we can use it
        }
        commandData.options = mutableListOf() // Initialize an empty list so we can use it

        declaration.subcommands.forEach {
            commandData.options?.add(convertSubcommandDeclarationToKord(it))
        }

        return commandData
    }

    private fun convertCommandOptionToKord(cmdOption: InteraKTionsCommandOption<*>, builder: BaseInputChatBuilder) {
        cmdOption.register(builder)
    }
}

public fun DiscordInteraKTions(token: String, applicationId: Snowflake): DiscordInteraKTions = DiscordInteraKTions(
    Kord.restOnly(token),
    applicationId
)