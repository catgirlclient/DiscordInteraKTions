package net.perfectdreams.discordinteraktions.common.requests

import net.perfectdreams.discordinteraktions.common.requests.managers.RequestManager
import net.perfectdreams.discordinteraktions.common.utils.Observable

/**
 * Bridges code between a interaction handler and the request managers
 *
 * Used to track [state] changes and to allow request managers to switch to other request managers within their own code
 *
 * **Don't forget to initialize the [manager] after creating the bridge!**
 *
 * @param state a [Observable] interaction request state
 */
public class RequestBridge(
    public val state: Observable<InteractionRequestState>
) {
    public var _manager: RequestManager? = null

    public var manager: RequestManager
        get() = _manager ?: throw IllegalArgumentException("RequestManager is null!")
        set(value) {
            _manager = value
        }
}