package live.shuuyu.discordinteraktions.platforms.kord.commands

import dev.kord.common.entity.*
import dev.kord.core.Kord
import io.github.oshai.kotlinlogging.KotlinLogging
import live.shuuyu.discordinteraktions.common.commands.*
import live.shuuyu.discordinteraktions.common.commands.options.OptionReference
import kotlin.reflect.KClass

public object CommandDeclarationUtils {
    private val logger = KotlinLogging.logger {}

    public fun getParentClass(thiz: Any): KClass<out Any> {
        val clazz = thiz::class
        val javaClazz = clazz.java
        val fullClassName = javaClazz.name
        if (!fullClassName.endsWith("\$Companion"))
            error("The class $fullClassName isn't a companion object, so we aren't able to find the parent class!")

        val parentClassName = fullClassName.removeSuffix("\$Companion")

        try {
            val parentClazz = javaClazz.classLoader.loadClass(parentClassName)
            return parentClazz.kotlin
        } catch (e: Exception) {
            logger.error(e) { "Couldn't load class $parentClassName! Are you using an anonymous class? If yes, then please set the $this's parent value manually!" }
            throw e
        }
    }

    /**
     * Finds all command declaration names in the [request]
     *
     * If a command only has one single command (example: `/loritta`) then the list will only have a single [CommandLabel]
     *
     * If a command has multiple subcommands and subgroups (example: `/loritta morenitta/cute`, where `morenitta/cute` is two subcommands) then the list
     * will have a [CommandLabel] and a [SubCommandLabel]
     *
     * @see getLabelsConnectedToSlashCommandDeclaration
     *
     * @param request the command interaction
     * @return a list with all of the labels
     */
    public fun findAllSubcommandDeclarationNames(request: DiscordInteraction): List<CommandLabel> {
        val commandLabels = mutableListOf<CommandLabel>(RootCommandLabel(request.data.name.value!!))
        val result = findAllSubcommandDeclarationNames(commandLabels, request.data.options.value)
        return result
    }

    private fun findAllSubcommandDeclarationNames(commandLabels: MutableList<CommandLabel>, options: List<Option>?): List<CommandLabel> {
        val firstOption = options?.firstOrNull()

        when (firstOption) {
            is SubCommand -> commandLabels.add(SubCommandLabel(firstOption.name))
            is CommandGroup -> commandLabels.add(CommandGroupLabel(firstOption.name))
            else -> return commandLabels
        }

        return commandLabels
    }

    /**
     * Gets the nested options in the [options]
     *
     * If the options are nested in `subcommand group` -> `subcommand` -> `command values`, this will return only the `command values`
     *
     * @param options the interaction options
     * @return the nested options
     */
    public fun getNestedOptions(options: List<Option>?): List<Option>? {
        val firstOption = options?.firstOrNull()

        if (firstOption is SubCommand) {
            return getNestedOptions(firstOption.options.value)
        } else if (firstOption is CommandGroup) {
            return getNestedOptions(firstOption.options.value)
        }

        return options
    }

    /**
     * Checks if the [labels] are connected from the [rootDeclaration] to the [declaration], by checking the [rootDeclaration] and its children until
     * the [declaration] is found.
     *
     * @see findAllSubcommandDeclarationNames
     *
     * @param labels          the request labels in order
     * @param declaration     the declaration that must be found
     * @return the matched declaration
     */
    public fun getLabelsConnectedToCommandDeclaration(labels: List<CommandLabel>, declaration: ApplicationCommandDeclaration): ApplicationCommandDeclaration? {
        if (declaration is SlashCommandDeclaration)
            return getLabelsConnectedToSlashCommandDeclaration(labels, declaration)

        // Let's not overcomplicate this, we already know that Discord only supports one level deep of nesting
        // (so group -> subcommand)
        // So let's do easy and quick checks
        if (labels.first() is RootCommandLabel && labels.first().label == declaration.name) {
            // Matches the root label! Yay!
            if (labels.size == 1)
            // If there is only a Root Label, then it means we found our root declaration!
                return declaration
        }
        return null
    }

    /**
     * Checks if the [labels] are connected from the [rootDeclaration] to the [declaration], by checking the [rootDeclaration] and its children until
     * the [declaration] is found.
     *
     * @see findAllSubcommandDeclarationNames
     *
     * @param labels          the request labels in order
     * @param declaration     the declaration that must be found
     * @return the matched declaration
     */
    private fun getLabelsConnectedToSlashCommandDeclaration(labels: List<CommandLabel>, declaration: SlashCommandDeclaration): SlashCommandDeclaration? {
        // Let's not over complicate this, we already know that Discord only supports one level deep of nesting
        // (so group -> subcommand)
        // So let's do easy and quick checks
        if (labels.first() is RootCommandLabel && labels.first().label == declaration.name) {
            // Matches the root label! Yay!
            if (labels.size == 1) {
                // If there is only a Root Label, then it means we found our root declaration!
                return declaration
            } else {
                val secondLabel = labels[1]

                // If not, let's check subcommand groups and subcommands
                // Thankfully we know when a label is a subcommand or a group!
                if (secondLabel is SubCommandLabel) {
                    for (subcommand in declaration.subcommands) {
                        if (secondLabel.label == subcommand.name) {
                            // Matches, then return this!
                            return subcommand
                        }
                    }
                    // Nothing found, return...
                    return null
                } else {
                    val thirdLabel = labels[2]

                    for (group in declaration.subcommandGroups) {
                        if (group.name == secondLabel.label) {
                            for (subcommand in group.subcommands) {
                                if (thirdLabel.label == subcommand.name) {
                                    // Matches, then return this!
                                    return subcommand
                                }
                            }
                        }
                    }
                    return null
                }
            }
        }
        return null
    }

    /**
     * Matches an application command declaration via the [commandLabels]
     *
     * @see getLabelsConnectedToSlashCommandDeclaration
     *
     * @param interactionsManager the command manager
     * @param commandLabels  the command labels
     * @return the matched declaration
     */
    public inline fun <reified T : ApplicationCommandDeclaration> getApplicationCommandDeclarationFromLabel(
        interactionsManager: InteractionsManager,
        commandLabels: List<CommandLabel>
    ): T? = interactionsManager.applicationCommandsDeclarations
        .asSequence()
        .filterIsInstance<T>()
        .mapNotNull {
            getLabelsConnectedToCommandDeclaration(
                commandLabels,
                it
            )
        }
        .firstOrNull() as T? // I don't know why this cast is needed

    public open class CommandLabel(public val label: String)
    public class RootCommandLabel(label: String) : CommandLabel(label)
    public class SubCommandLabel(label: String) : CommandLabel(label)
    public class CommandGroupLabel(label: String) : CommandLabel(label)

    public fun convertOptions(
        kord: Kord,
        request: DiscordInteraction,
        executor: SlashCommandExecutor,
        relativeOptions: List<Option>
    ): Map<OptionReference<*>, Any?> {
        val arguments = mutableMapOf<OptionReference<*>, Any?>()
        val options = relativeOptions.filterIsInstance<CommandArgument<*>>()

        for (declarationOption in executor.options.registeredOptions) {
            val optionReference = executor.options.references
                .firstOrNull { it.name == declarationOption.name } ?: continue

            arguments[optionReference] = declarationOption.parse(kord, options, request)
        }

        return arguments
    }
}