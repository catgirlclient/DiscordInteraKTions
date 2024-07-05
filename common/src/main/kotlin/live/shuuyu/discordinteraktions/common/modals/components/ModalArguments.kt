package live.shuuyu.discordinteraktions.common.modals.components

public class ModalArguments(public val types: Map<ComponentReference<*>, Any?>) {
    public operator fun <T> get(argument: ComponentReference<T>): T {
        if (!types.containsKey(argument) && argument.required)
            throw RuntimeException("Missing argument ${argument.customId}!")

        return types[argument] as T
    }
}