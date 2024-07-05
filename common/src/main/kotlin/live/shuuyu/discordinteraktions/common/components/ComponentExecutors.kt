package live.shuuyu.discordinteraktions.common.components

import dev.kord.core.entity.User

public sealed interface ComponentExecutor {
    /**
     * Used by the [ComponentExecutorDeclaration] to match declarations to executors.
     *
     * By default, the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    public fun signature(): Any = this::class
}

// ===[ BUTTONS ]===
public interface ButtonExecutor : ComponentExecutor {
    public suspend fun onClick(user: User, context: ComponentContext)
}

// ===[ SELECT MENUS ]===
public interface SelectMenuExecutor : ComponentExecutor {
    public suspend fun onSelect(user: User, context: ComponentContext, values: List<String>)
}