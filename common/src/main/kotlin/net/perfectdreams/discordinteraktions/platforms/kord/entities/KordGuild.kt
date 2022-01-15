package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuild
import net.perfectdreams.discordinteraktions.common.entities.Guild

class KordGuild(private val guild: DiscordGuild) : Guild {
    override val id = guild.id
}