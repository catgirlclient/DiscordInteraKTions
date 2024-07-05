package net.perfectdreams.discordinteraktions.webserver

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import net.perfectdreams.discordinteraktions.common.DiscordInteraKTions
import net.perfectdreams.discordinteraktions.common.commands.InteractionsManager

/**
 * Class represents a Rest Interactions Server, which connects
 * to the Discord API and wrap your requests.
 *
 * @param applicationId Your bot ID/Client ID (https://i.imgur.com/075OBWk.png)
 * @param publicKey The public key of your bot (https://i.imgur.com/xDZnJ5J.png)
 * @param token Your bot token (https://i.imgur.com/VXLOFte.png)
 * @param port HTTP server port to bind
 */
public class InteractionsServer(
    public val interaKTions: DiscordInteraKTions,
    public val publicKey: String,
    public val port: Int = 12212,
) {
    public companion object {
        public val json: Json = Json {
            // If there's any unknown keys, we'll ignore them instead of throwing an exception.
            this.ignoreUnknownKeys = true
        }
        private val logger = KotlinLogging.logger {}
    }

    public val interactionsManager: InteractionsManager = InteractionsManager()
    public val interactionRequestHandler: InteractionRequestHandler = DefaultInteractionRequestHandler(interaKTions)

    /**
     * You can use this method to start the interactions server,
     * which will open a connection on the 12212 port with the **Netty** engine.
     */
    public fun start() {
        val server = embeddedServer(Netty, port = port) {
            routing {
                get("/") {
                    call.respondText("Hello, Discord Interactions!")
                }

                installDiscordInteractions(
                    publicKey,
                    "/",
                    interactionRequestHandler
                )
            }
        }

        server.start(true)
    }
}
