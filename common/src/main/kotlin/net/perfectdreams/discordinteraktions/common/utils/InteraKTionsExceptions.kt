package net.perfectdreams.discordinteraktions.common.utils

public object InteraKTionsExceptions {
    public fun missingDeclaration(type: String): Nothing =
        error("The %s declaration wasn't found! Did you register the %s declaration?".format(type, type))
    public fun missingExecutor(type: String): Nothing =
        error("The %s executor wasn't found! Did you register the %s executor?".format(type, type))
}