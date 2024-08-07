package live.shuuyu.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ComponentType
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.cache.data.MemberData
import dev.kord.core.cache.data.UserData
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.shuuyu.discordinteraktions.common.commands.InteractionsManager
import live.shuuyu.discordinteraktions.common.components.ComponentContext
import live.shuuyu.discordinteraktions.common.components.ComponentExecutorDeclaration
import live.shuuyu.discordinteraktions.common.components.GuildComponentContext
import live.shuuyu.discordinteraktions.common.entities.messages.Message
import live.shuuyu.discordinteraktions.common.interactions.InteractionData
import live.shuuyu.discordinteraktions.common.requests.RequestBridge
import live.shuuyu.discordinteraktions.common.requests.managers.RequestManager
import live.shuuyu.discordinteraktions.common.utils.InteraKTionsExceptions
import live.shuuyu.discordinteraktions.platforms.kord.entities.messages.KordPublicMessage

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
public class KordComponentChecker(public val kord: Kord, public val interactionsManager: InteractionsManager) {
    public fun checkAndExecute(request: DiscordInteraction, requestManager: RequestManager) {
        val bridge = requestManager.bridge

        val componentType = request.data.componentType.value ?: error("Component Type is not present in Discord's request! Bug?")

        // If the component doesn't have a custom ID, we won't process it
        val componentCustomId = request.data.customId.value ?: return

        val executorId = componentCustomId.substringBefore(":")
        val data = componentCustomId.substringAfter(":")

        val kordUser = User(
            UserData.from(request.member.value?.user?.value ?: request.user.value ?: error("oh no")),
            kord
        )

        val kordPublicMessage =
            KordPublicMessage(kord, request.message.value!!)

        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects(kord, guildId))

        // Now this changes a bit depending on what we are trying to execute
        when (componentType) {
            is ComponentType.Unknown -> error("Unknown Component Type!")
            ComponentType.ActionRow -> error("Received a ActionRow component interaction... but that's impossible!")
            ComponentType.Button -> {
                val executorDeclaration = interactionsManager.componentExecutorDeclarations
                    .asSequence()
                    .filter {
                        it.id == executorId
                    }
                    .firstOrNull() ?: InteraKTionsExceptions.missingDeclaration("button")

                val executor = interactionsManager.buttonExecutors.firstOrNull {
                    it.signature() == executorDeclaration.parent
                } ?: InteraKTionsExceptions.missingExecutor("button")

                GlobalScope.launch {
                    executor.onClick(
                        kordUser,
                        createContext(
                            executorDeclaration,
                            bridge,
                            kordUser,
                            request,
                            interactionData,
                            guildId,
                            kordPublicMessage,
                            data
                        )
                    )
                }
            }
            // If I am not wrong then these should follow the same concepts either way
            ComponentType.ChannelSelect, ComponentType.StringSelect, ComponentType.MentionableSelect,
            ComponentType.RoleSelect, ComponentType.UserSelect -> {
                val executorDeclaration = interactionsManager.componentExecutorDeclarations
                    .asSequence()
                    .filter {
                        it.id == executorId
                    }
                    .firstOrNull() ?: InteraKTionsExceptions.missingDeclaration("select menu")

                val executor = interactionsManager.selectMenusExecutors.firstOrNull {
                    it.signature() == executorDeclaration.parent
                } ?: InteraKTionsExceptions.missingExecutor("select menu")

                GlobalScope.launch {
                    executor.onSelect(
                        kordUser,
                        createContext(
                            executorDeclaration,
                            bridge,
                            kordUser,
                            request,
                            interactionData,
                            guildId,
                            kordPublicMessage,
                            data
                        ),
                        request.data.values.value ?: error("Values list is null!")
                    )
                }
            }
            ComponentType.TextInput -> error("Text inputs are only allowed inside of modals!")
            else -> error("what")
        }
    }

    private fun createContext(
        declaration: ComponentExecutorDeclaration,
        bridge: RequestBridge,
        kordUser: User,
        request: DiscordInteraction,
        interactionData: InteractionData,
        guildId: Snowflake?,
        message: Message,
        data: String
    ): ComponentContext {
        // If the guild ID is not null, then it means that the interaction happened in a guild!
        return if (guildId != null) {
            val kordMember = Member(
                MemberData.from(kordUser.id, guildId, request.member.value!!), // Should NEVER be null!
                kordUser.data,
                kord
            )

            GuildComponentContext(
                bridge,
                kordUser,
                request.channelId.value!!,
                declaration,
                message,
                data.ifEmpty { null },
                interactionData,
                request,
                guildId,
                kordMember
            )
        } else {
            ComponentContext(
                bridge,
                kordUser,
                request.channelId.value!!,
                declaration,
                message,
                data.ifEmpty { null },
                interactionData,
                request
            )
        }
    }
}