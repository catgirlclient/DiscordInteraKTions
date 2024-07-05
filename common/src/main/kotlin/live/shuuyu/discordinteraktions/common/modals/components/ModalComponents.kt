package live.shuuyu.discordinteraktions.common.modals.components

import dev.kord.common.entity.TextInputStyle
import live.shuuyu.discordinteraktions.common.commands.options.OptionReference

public open class ModalComponents {
    public val registeredComponents: MutableList<InteraKTionsModalComponent<*>> = mutableListOf()
    public val references: MutableList<ComponentReference<*>> = mutableListOf()

    public fun textInput(
        customId: String,
        style: TextInputStyle,
        block: TextInputModalComponentBehaviorBuilder.() -> (Unit) = {}
    ): ComponentReference<String> = TextInputModalComponentBehaviorBuilder(customId, style)
        .apply(block)
        .let {
            register(it)
        }

    public fun optionalTextInput(
        customId: String,
        style: TextInputStyle,
        block: NullableTextInputModalComponentBehaviorBuilder.() -> (Unit) = {}
    ): ComponentReference<String?> = NullableTextInputModalComponentBehaviorBuilder(customId, style)
        .apply(block)
        .let {
            register(it)
        }
}


/**
 * Registers a [componentBuilder] to an [ModalComponents]
 *
 * @param componentBuilder the option builder
 * @return an [OptionReference]
 */
public inline fun <reified T, ChoiceableType> ModalComponents.register(
    componentBuilder: ModalComponentBehaviorBuilder<T, ChoiceableType>
): ComponentReference<T> {
    if (registeredComponents.any { it.customId == componentBuilder.id })
        throw IllegalArgumentException("Duplicate argument \"${componentBuilder.id}\"!")

    val componentReference = ComponentReference<T>(this, componentBuilder.id, componentBuilder.required)
    registeredComponents.add(componentBuilder.build())
    references.add(componentReference)

    return componentReference
}