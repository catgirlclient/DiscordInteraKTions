package net.perfectdreams.discordinteraktions.common.commands

public interface ApplicationCommandDeclarationWrapper

public interface SlashCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper {
    public fun declaration(): SlashCommandDeclarationBuilder
}

public interface UserCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper {
    public fun declaration(): UserCommandDeclarationBuilder
}

public interface MessageCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper {
    public fun declaration(): MessageCommandDeclarationBuilder
}