package live.shuuyu.discordinteraktions.common.autocomplete

public fun interface AutocompleteHandler<T> {
    public suspend fun handle(context: AutocompleteContext, focusedOption: FocusedCommandOption): Map<String, T>
}