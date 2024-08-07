package live.shuuyu.discordinteraktions.common.modals.components

import dev.kord.rest.builder.component.ActionRowBuilder

public fun <T : String?> ActionRowBuilder.textInput(
    reference: ComponentReference<T>,
    label: String,
    block: TextInputComponentAppearanceBuilder.() -> (Unit) = {}
) {
    val componentBehavior = reference.components.registeredComponents.first { it.customId == reference.customId }
            as TextInputModalComponent

    val builder = TextInputComponentAppearanceBuilder().apply(block)

    val minRange = componentBehavior.minLength ?: 0
    val maxRange = componentBehavior.maxLength ?: 4000

    this.textInput(componentBehavior.style, componentBehavior.customId, label) {
        this.required = componentBehavior.required
        this.placeholder = builder.placeholder
        this.allowedLength = minRange..maxRange
        this.placeholder = builder.placeholder
        this.value = builder.value
    }
}