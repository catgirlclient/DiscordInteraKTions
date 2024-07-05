package live.shuuyu.discordinteraktions.common.components

import live.shuuyu.discordinteraktions.common.components.ComponentExecutorDeclaration.Companion.ID_REGEX
import live.shuuyu.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils

public sealed class ComponentExecutorDeclaration(
    /**
     * The [parent] is Any? to avoid issues with anonymous classes
     *
     * When using anonymous classes, you can use another type to match declarations
     *
     * If [parent] is null when the class is initialized, the declaration will try to find the parent by using Reflection!
     */
    public var parent: Any? = null,

    /**
     * The executor's ID, this is stored in the button, to be able to figure out what executor should be used
     *
     * All button executors should be unique!
     */
    public val id: String,

    /**
     * The RegEx that the [id] will be validated against
     *
     * By default, it will be validated against the [ID_REGEX] regex, but you can use another RegEx if you want to
     *
     * Keep in mind that some IDs may break Discord InteraKTions functionality, such as IDs containing ":"
     */
    idRegex: Regex = ID_REGEX
) {
    private constructor(id: String, idRegex: Regex = ID_REGEX) : this(null, id, idRegex)

    public companion object {
        public val ID_REGEX: Regex = Regex("[A-z0-9]+")
    }

    init {
        require(idRegex.matches(id)) { "ID must respect the $ID_REGEX regular expression!" }

        if (parent == null)
            parent = CommandDeclarationUtils.getParentClass(this)
    }
}

public open class ButtonExecutorDeclaration(
    parent: Any? = null,
    id: String
) : ComponentExecutorDeclaration(parent, id) {
    public constructor(id: String) : this(null, id)
}

public open class SelectMenuExecutorDeclaration(
    parent: Any? = null,
    id: String
) : ComponentExecutorDeclaration(parent, id) {
    public constructor(id: String) : this(null, id)
}