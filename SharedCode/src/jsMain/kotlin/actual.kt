/**
 * @author SIY1121
 */
package space.siy.kotlinmultiplatformchat

import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.launch
import kotlin.browser.window
import kotlinx.serialization.*
import kotlinx.serialization.json.*

actual fun platformName() = window.navigator.userAgent

actual fun ioDispatcher(): CoroutineDispatcher {
    return Dispatchers.Main
}

actual fun mainDispatcher(): CoroutineDispatcher {
    return Dispatchers.Main
}

actual class Sender actual constructor(private val host: String, private val port: Int) {
    actual var onMessage: (suspend (Message) -> Unit)? = null
    actual var onReady: (suspend () -> Unit)? = null
    val json = Json(JsonConfiguration.Stable)

    private lateinit var session: WebSocketSession

    val client = HttpClient {
        install(WebSockets)
    }

    actual fun setup() {
        GlobalScope.launch(ioDispatcher()) {
            client.ws("ws://$host:$port") {
                session = this
                onReady?.invoke()
                 for(frame in incoming) {
                    when (frame) {
                        is Frame.Text -> onMessage?.invoke(json.parse(Message.serializer(), frame.readText()))
                        else -> {
                        }
                    }
                }

            }
        }
    }

    @ImplicitReflectionSerializer
    actual fun send(msg: Message) {
        GlobalScope.launch(ioDispatcher()) {
            session.send(Frame.Text(json.stringify(msg)))
        }
    }
}