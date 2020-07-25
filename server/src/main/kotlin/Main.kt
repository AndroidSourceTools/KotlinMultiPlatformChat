package space.siy.multiplatformchat

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.request.receive
import io.ktor.request.receiveOrNull
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import io.ktor.serialization.serialization
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSocketServerSession
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.stringify

/**
 * @author SIY1121
 */

@KtorExperimentalAPI
fun main() {
    embeddedServer(CIO, 8081) {
        val room = Room()

        install(WebSockets)
        install(ContentNegotiation) {
            json(
                json = Json(
                    DefaultJsonConfiguration.copy(
                        prettyPrint = true
                    )
                ),
                contentType = ContentType.Application.Json
            )
        }

        routing {
            webSocket("/") {
                room.join(this)
            }
            get("/msg") {
                val offset = call.parameters["offset"]?.toIntOrNull()
                if (offset == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                if (room.messages.size > offset)
                    call.respond(room.messages.subList(offset, room.messages.size))
                else {
                    repeat(5) {
                        delay(1000)
                        if (room.messages.size > offset) {
                            call.respond(room.messages.subList(offset, room.messages.size))
                            return@get
                        }
                    }
                    call.respond(emptyArray<Message>())
                }

            }
            post("/msg") {
                val msg = call.receive<Message>()
                call.respond(HttpStatusCode.OK)
                room.broadcast(msg)
            }
        }
    }.start(wait = true)
}

@Serializable
data class Message(val user: String, val content: String)

class Room {
    val json = Json(JsonConfiguration.Stable)
    val sessions: MutableList<WebSocketServerSession> = ArrayList()
    val messages: MutableList<Message> = ArrayList()
    suspend fun join(session: WebSocketServerSession) {
        sessions.add(session)
        session.run {
            for(frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val msg = json.parse(Message.serializer(), frame.readText())
                        broadcast(msg)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    suspend fun broadcast(msg: Message) {
        synchronized(messages) {
            messages.add(msg)
        }
        println(msg)
        sessions.forEach {
            it.send(Frame.Text(json.stringify(Message.serializer(), msg)))
        }
    }
}