package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.TextInputStyle

public abstract class ModalComponentBehaviorBuilder<T, ChoiceableType>(
    public val id: String,
    public val required: Boolean
) {
    public abstract fun build(): InteraKTionsModalComponent<*>
}

// ===[ TEXT INPUT ]===
public abstract class TextInputModalComponentBehaviorBuilderBase<T>(
    id: String,
    required: Boolean,
    public val style: TextInputStyle
) : ModalComponentBehaviorBuilder<T, String>(id, required) {
    public var minLength: Int? = null
    public var maxLength: Int? = null
    public var allowedLength: IntRange
        get() = error("This is a settable property only")
        set(value) {
            minLength = value.first
            maxLength = value.last
        }

    override fun build(): TextInputModalComponent = TextInputModalComponent(
        id,
        required,
        style,
        minLength,
        maxLength
    )
}

public class TextInputModalComponentBehaviorBuilder(
    id: String,
    style: TextInputStyle
) : TextInputModalComponentBehaviorBuilderBase<String>(id, true, style)

public class NullableTextInputModalComponentBehaviorBuilder(
    id: String,
    style: TextInputStyle
) : TextInputModalComponentBehaviorBuilderBase<String?>(id, false, style)