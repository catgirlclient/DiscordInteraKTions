package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.common.components.*
import net.perfectdreams.discordinteraktions.common.modals.ModalExecutor
import net.perfectdreams.discordinteraktions.common.modals.ModalExecutorDeclaration

public open class InteractionsManager {
    public val applicationCommandsDeclarations: MutableList<ApplicationCommandDeclaration> = mutableListOf()
    public val applicationCommandsExecutors: List<ApplicationCommandExecutor>
        get() = userCommandsExecutors + messageCommandsExecutors + slashCommandsExecutors

    public val userCommandsExecutors: List<UserCommandExecutor>
        get() = applicationCommandsDeclarations
            .filterIsInstance<UserCommandDeclaration>()
            .map { it.executor }

    public val messageCommandsExecutors: List<MessageCommandExecutor>
        get() = applicationCommandsDeclarations
            .filterIsInstance<MessageCommandDeclaration>()
            .map { it.executor }

    public val slashCommandsExecutors: List<SlashCommandExecutor>
        get() = applicationCommandsDeclarations
            .filterIsInstance<SlashCommandDeclaration>()
            .flatMap { it.subcommands.map { it.executor } + it.subcommandGroups.flatMap { it.subcommands.map { it.executor } } + it.executor }
            .filterNotNull()

    public val componentExecutorDeclarations: MutableList<ComponentExecutorDeclaration> = mutableListOf()
    public val buttonExecutors: MutableList<ButtonExecutor> = mutableListOf()
    public val selectMenusExecutors: MutableList<SelectMenuExecutor> = mutableListOf()

    public val modalDeclarations: MutableList<ModalExecutorDeclaration> = mutableListOf()
    public val modalExecutors: MutableList<ModalExecutor> = mutableListOf()

    public fun register(declarationWrapper: SlashCommandDeclarationWrapper) {
        val builder = declarationWrapper.declaration()
        register(builder.build())
    }

    public fun register(declarationWrapper: MessageCommandDeclarationWrapper) {
        val builder = declarationWrapper.declaration()
        register(builder.build())
    }

    public fun register(declarationWrapper: UserCommandDeclarationWrapper) {
        val builder = declarationWrapper.declaration()
        register(builder.build())
    }

    public fun register(declaration: ApplicationCommandDeclaration) {
        // TODO: Validate if all executors of the command are present
        if (applicationCommandsDeclarations.any { it.name == declaration.name })
            error("There's already an root command registered with the label ${declaration.name}!")

        applicationCommandsDeclarations.add(declaration)
    }

    public fun register(executor: ButtonExecutor) {
        val declaration = getComponentExecutorDeclarationFromExecutor(executor)
        register(declaration, executor)
    }

    public fun register(executor: SelectMenuExecutor) {
        val declaration = getComponentExecutorDeclarationFromExecutor(executor)
        register(declaration, executor)
    }

    public fun register(declaration: ComponentExecutorDeclaration, executor: ButtonExecutor) {
        if (componentExecutorDeclarations.any { it.id == declaration.id })
            error("There's already a component executor registered with the ID ${declaration.id}!")

        componentExecutorDeclarations.add(declaration)
        buttonExecutors.add(executor)
    }

    public fun register(declaration: ComponentExecutorDeclaration, executor: SelectMenuExecutor) {
        if (componentExecutorDeclarations.any { it.id == declaration.id })
            error("There's already a component executor registered with the ID ${declaration.id}!")

        componentExecutorDeclarations.add(declaration)
        selectMenusExecutors.add(executor)
    }

    public fun register(executor: ModalExecutor) {
        val declaration = getModalExecutorDeclarationFromExecutor(executor)
        register(declaration, executor)
    }

    public fun register(declaration: ModalExecutorDeclaration, executor: ModalExecutor) {
        modalDeclarations.add(declaration)
        modalExecutors.add(executor)
    }

    private fun getComponentExecutorDeclarationFromExecutor(executor: Any): ComponentExecutorDeclaration {
        return executor::class.nestedClasses.mapNotNull { it.objectInstance }
            .filterIsInstance<ComponentExecutorDeclaration>()
            .firstOrNull() ?: error("Missing ComponentExecutorDeclaration on $executor's executor!")
    }

    private fun getModalExecutorDeclarationFromExecutor(executor: Any): ModalExecutorDeclaration {
        return executor::class.nestedClasses.mapNotNull { it.objectInstance }
            .filterIsInstance<ModalExecutorDeclaration>()
            .firstOrNull() ?: error("Missing ModalSubmitExecutorDeclaration on $executor's executor!")
    }
}