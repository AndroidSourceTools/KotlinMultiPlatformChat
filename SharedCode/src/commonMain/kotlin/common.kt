/**
 * @author SIY1121
 */

package space.siy.kotlinmultiplatformchat

import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.client.request.get
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.*
import kotlinx.serialization.json.*

expect fun platformName(): String

fun createApplicationScreenMessage() = "Kotlin on ${platformName()}"

expect fun ioDispatcher(): CoroutineDispatcher
expect fun mainDispatcher(): CoroutineDispatcher

fun fetchRemoteMessage(done: (res: String) -> Unit) {
    GlobalScope.launch(ioDispatcher()) {
        val client = HttpClient()
        println("http client created")
        val res = client.get<String>("https://jsonplaceholder.typicode.com/todos/1")
        println("http request done")
        withContext(mainDispatcher()) {
            done(res)
        }
    }
}

@Serializable
data class Message(val user: String, val content: String)

expect class Sender(host: String, port: Int) {
    var onMessage: (suspend (Message) -> Unit)?
    var onReady: (suspend () -> Unit)?
    fun setup()
    fun send(msg: Message)
}

class ChatService(val host: String, val port: Int, val name: String) {

    val sender = Sender(host, port)

    var onMessage: ((msg: Message) -> Unit)? = null
    var onReady: (() -> Unit)? = null

    fun connect() {
        sender.onMessage = {
            withContext(mainDispatcher()) { onMessage?.invoke(it) }
        }

        sender.onReady = {
            withContext(mainDispatcher()) { onReady?.invoke()}
        }
        sender.setup()
    }

    @ImplicitReflectionSerializer
    fun send(msg: String) {
        sender.send(Message(name, msg))
    }
}