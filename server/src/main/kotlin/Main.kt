package space.siy.multiplatformchat

import io.ktor.application.install
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.receiveOrNull

/**
 * @author SIY1121
 */

fun main() {
    embeddedServer(Netty, 8080) {
        install(WebSockets)
        routing {
            webSocket("/") {
                while (true) {
                    val frame = incoming.receiveOrNull() ?: break
                    when (frame) {
                        is Frame.Text -> outgoing.send(Frame.Text("echo: ${frame.readText()}"))
                        else -> {
                        }
                    }
                }
            }
        }
    }.start()
}