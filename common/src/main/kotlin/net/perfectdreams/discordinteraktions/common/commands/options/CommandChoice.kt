package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale

public class CommandChoice<T>(
    public val name: String,
    public val value: T,
    public val nameLocalizations: Map<Locale, String>?
)