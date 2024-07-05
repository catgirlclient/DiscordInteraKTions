package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionResponseType
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import net.perfectdreams.discordinteraktions.common.DiscordInteraKTions
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.webserver.requests.managers.WebServerRequestManager

/**
 * This is the default implementation of [InteractionRequestHandler],
 * and the server's built-in request handler too (you can change that on the [InteractionsServer] class)
 *
 * @param m The server that we'll handle the requests for.
 */
public class DefaultInteractionRequestHandler(public val interaKTions: DiscordInteraKTions) : InteractionRequestHandler() {
    public companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onPing(call: ApplicationCall) {
        logger.info { "Ping Request Received!" }
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.Pong.type)
            }.toString(),
            ContentType.Application.Json
        )
    }

    override suspend fun onCommand(call: ApplicationCall, request: DiscordInteraction) {
        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = WebServerRequestManager(
            bridge,
            interaKTions.kord,
            interaKTions.applicationId,
            request.token,
            call
        )

        bridge.manager = requestManager

        interaKTions.commandChecker.checkAndExecute(
            request,
            requestManager
        )

        observableState.awaitChange()
        logger.info { "State was changed to ${observableState.value}, so this means we already replied via the Web Server! Leaving request scope..." }
    }

    override suspend fun onComponent(call: ApplicationCall, request: DiscordInteraction) {
        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = WebServerRequestManager(
            bridge,
            interaKTions.kord,
            interaKTions.applicationId,
            request.token,
            call
        )

        bridge.manager = requestManager

        interaKTions.componentChecker.checkAndExecute(
            request,
            requestManager
        )

        observableState.awaitChange()
        logger.info { "State was changed to ${observableState.value}, so this means we already replied via the Web Server! Leaving request scope..." }
    }

    override suspend fun onAutocomplete(call: ApplicationCall, request: DiscordInteraction) {
        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = WebServerRequestManager(
            bridge,
            interaKTions.kord,
            interaKTions.applicationId,
            request.token,
            call
        )

        bridge.manager = requestManager

        interaKTions.autocompleteChecker.checkAndExecute(
            request,
            requestManager
        )

        observableState.awaitChange()
        logger.info { "State was changed to ${observableState.value}, so this means we already replied via the Web Server! Leaving request scope..." }
    }

    override suspend fun onModalSubmit(call: ApplicationCall, request: DiscordInteraction) {
        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = WebServerRequestManager(
            bridge,
            interaKTions.kord,
            interaKTions.applicationId,
            request.token,
            call
        )

        bridge.manager = requestManager

        interaKTions.modalChecker.checkAndExecute(
            request,
            requestManager
        )

        observableState.awaitChange()
        logger.info { "State was changed to ${observableState.value}, so this means we already replied via the Web Server! Leaving request scope..." }
    }
}
