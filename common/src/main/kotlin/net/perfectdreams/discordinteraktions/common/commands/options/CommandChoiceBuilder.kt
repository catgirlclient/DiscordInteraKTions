package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale

public class CommandChoiceBuilder<T>(
    public val name: String,
    public val value: T
) {
    public var nameLocalizations: Map<Locale, String>? = null

    public fun build(): CommandChoice<T> = CommandChoice(
        name,
        value,
        nameLocalizations
    )
}