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

class ChatService(val url: String) {
    data class Message(val user: String, val content: String)

    val client = HttpClient {
        install(WebSockets)
    }

    var onMessage: ((msg: Message) -> Unit)? = null

    fun connect() {
        GlobalScope.launch(ioDispatcher()) {
            client.ws(url) {
                outgoing.send(Frame.Text("hello"))
                while(true) {
                    val frame = incoming.receiveOrNull() ?: break
                    when(frame) {
                        is Frame.Text -> {
                            val text = frame.readText().split(" ")
                            withContext(mainDispatcher()) {
                                onMessage?.invoke(Message(text[0], text[1]))
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}