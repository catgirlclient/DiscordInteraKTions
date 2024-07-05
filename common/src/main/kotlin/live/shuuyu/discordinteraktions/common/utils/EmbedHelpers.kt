package live.shuuyu.discordinteraktions.common.utils

// Utilities extension methods for Kord Embeds, mostly providing functions to replace Kord's embed callbacks
public fun dev.kord.rest.builder.message.EmbedBuilder.author(name: String, url: String? = null, iconUrl: String? = null) {
    author {
        this.name = name
        this.url = url
        this.icon = iconUrl
    }
}

public var dev.kord.rest.builder.message.EmbedBuilder.thumbnailUrl: String?
    get() = this.thumbnail?.url
    set(value) {
        if (value == null) {
            this.thumbnail = null
        } else {
            thumbnail {
                this.url = value
            }
        }
    }

public fun dev.kord.rest.builder.message.EmbedBuilder.footer(text: String, iconUrl: String? = null) {
    footer {
        this.text = text
        this.icon = iconUrl
    }
}

public fun dev.kord.rest.builder.message.EmbedBuilder.field(name: String, value: String, inline: Boolean = false): Unit =
    field(name, inline) { value }

public fun dev.kord.rest.builder.message.EmbedBuilder.inlineField(name: String, value: String): Unit =
    field(name, true) { value }