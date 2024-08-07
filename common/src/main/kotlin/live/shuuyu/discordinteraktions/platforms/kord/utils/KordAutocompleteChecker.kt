package live.shuuyu.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.Choice
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.core.Kord
import dev.kord.core.cache.data.MemberData
import dev.kord.core.cache.data.UserData
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import dev.kord.rest.builder.interaction.IntegerOptionBuilder
import dev.kord.rest.builder.interaction.NumberOptionBuilder
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.shuuyu.discordinteraktions.common.autocomplete.AutocompleteContext
import live.shuuyu.discordinteraktions.common.autocomplete.FocusedCommandOption
import live.shuuyu.discordinteraktions.common.autocomplete.GuildAutocompleteContext
import live.shuuyu.discordinteraktions.common.commands.InteractionsManager
import live.shuuyu.discordinteraktions.common.commands.SlashCommandDeclaration
import live.shuuyu.discordinteraktions.common.commands.options.ChoiceableCommandOption
import live.shuuyu.discordinteraktions.common.commands.options.IntegerCommandOption
import live.shuuyu.discordinteraktions.common.commands.options.NumberCommandOption
import live.shuuyu.discordinteraktions.common.commands.options.StringCommandOption
import live.shuuyu.discordinteraktions.common.interactions.InteractionData
import live.shuuyu.discordinteraktions.common.requests.managers.RequestManager
import live.shuuyu.discordinteraktions.common.utils.InteraKTionsExceptions
import live.shuuyu.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
public class KordAutocompleteChecker(public val kord: Kord, public val interactionsManager: InteractionsManager) {
    public companion object {
        private val logger = KotlinLogging.logger {}
    }

    public fun checkAndExecute(request: DiscordInteraction, requestManager: RequestManager) {
        val bridge = requestManager.bridge

        logger.debug { request.data.name }

        // Processing subcommands is kinda hard, but not impossible!
        val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(request)
        val relativeOptions = CommandDeclarationUtils.getNestedOptions(request.data.options.value)
            ?: error("Relative Options are null on the request, this shouldn't happen on a autocomplete request! Bug?")

        val kordUser = User(
            UserData.from(request.member.value?.user?.value ?: request.user.value ?: error("oh no")),
            kord
        )
        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects(kord, guildId))

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val autocompleteContext = if (guildId != null) {
            val kordMember = Member(
                MemberData.from(kordUser.id, guildId, request.member.value!!), // Should NEVER be null!
                kordUser.data,
                kord
            )

            GuildAutocompleteContext(
                kordUser,
                request.channelId.value!!,
                interactionData,
                relativeOptions.filterIsInstance<CommandArgument<*>>(),
                request,
                guildId,
                kordMember
            )
        } else {
            AutocompleteContext(
                kordUser,
                request.channelId.value!!,
                interactionData,
                relativeOptions.filterIsInstance<CommandArgument<*>>(),
                request
            )
        }

        val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<SlashCommandDeclaration>(interactionsManager, commandLabels)
            ?: InteraKTionsExceptions.missingDeclaration("slash command")

        val focusedDiscordOption = relativeOptions.filterIsInstance<CommandArgument.AutoCompleteArgument>()
            .firstOrNull() ?: error("There isn't any autocomplete option on the autocomplete request! Bug?")

        require(focusedDiscordOption.focused.discordBoolean) { "Autocomplete argument is not set to focused! Bug?" }

        val slashCommandExecutor = command.executor ?: return

        val option = slashCommandExecutor.options.registeredOptions.firstOrNull {
            it.name == focusedDiscordOption.name
        } ?: error("I couldn't find a matching option for ${focusedDiscordOption.name}! Did you update the application command body externally?")

        require(option is ChoiceableCommandOption<*>) { "Command option is not choiceable, so it can't be autocompleted! Bug?" }

        GlobalScope.launch {
            val focusedCommandOption = FocusedCommandOption(
                focusedDiscordOption.name,
                focusedDiscordOption.value
            )

            when (option) {
                is StringCommandOption -> {
                    val autocompleteExecutor = option.autocompleteExecutor ?: error("Received autocomplete request for ${focusedDiscordOption.name}, but there isn't any autocomplete executor declaration set on the option! Did you update the application command body externally?")

                    val autocompleteResult = autocompleteExecutor.handle(autocompleteContext, focusedCommandOption)
                    bridge.manager.sendStringAutocomplete(
                        (StringChoiceBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices ?: listOf()) as List<Choice.StringChoice>
                    )
                }

                is IntegerCommandOption -> {
                    val autocompleteExecutor = option.autocompleteExecutor ?: error("Received autocomplete request for ${focusedDiscordOption.name}, but there isn't any autocomplete executor declaration set on the option! Did you update the application command body externally?")

                    val autocompleteResult = autocompleteExecutor.handle(autocompleteContext, focusedCommandOption)
                    bridge.manager.sendIntegerAutocomplete(
                        (IntegerOptionBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices ?: listOf()) as List<Choice.IntegerChoice>
                    )
                }

                is NumberCommandOption -> {
                    val autocompleteExecutor = option.autocompleteExecutor ?: error("Received autocomplete request for ${focusedDiscordOption.name}, but there isn't any autocomplete executor declaration set on the option! Did you update the application command body externally?")

                    val autocompleteResult = autocompleteExecutor.handle(autocompleteContext, focusedCommandOption)
                    bridge.manager.sendNumberAutocomplete(
                        NumberOptionBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices as List<Choice.NumberChoice>
                    )
                }

                else -> error("Unsupported Autocomplete type ${option::class}")
            }
        }
    }
}