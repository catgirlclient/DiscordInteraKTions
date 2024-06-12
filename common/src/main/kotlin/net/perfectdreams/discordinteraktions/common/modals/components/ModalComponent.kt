package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.TextInputStyle
import dev.kord.rest.builder.interaction.BaseInputChatBuilder

public abstract class InteraKTionsModalComponent<T>(
    public val customId: String,
    public val required: Boolean
) {
    public abstract fun register(builder: BaseInputChatBuilder)

    public abstract fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): T?
}

public class TextInputModalComponent(
    customId: String,
    required: Boolean,
    public val style: TextInputStyle,
    public val minLength: Int?,
    public val maxLength: Int?
) : InteraKTionsModalComponent<String>(customId, required) {
    override fun register(builder: BaseInputChatBuilder) {
        TODO("Not yet implemented")
    }

    override fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): String? {
        TODO("Not yet implemented")
    }
}