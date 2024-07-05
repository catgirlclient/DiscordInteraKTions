package live.shuuyu.discordinteraktions.common.commands.options

public class SlashCommandArguments(public val types: Map<OptionReference<*>, Any?>) {
    public operator fun <T> get(argument: OptionReference<T>): T {
        if (!types.containsKey(argument) && argument.required)
            throw RuntimeException("Missing argument ${argument.name}!")

        return types[argument] as T
    }
}