package space.siy.kotlinmultiplatformchat

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import platform.UIKit.UIDevice
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

/**
 * @author SIY1121
 */

actual fun platformName() = """
    ${UIDevice.currentDevice.systemName}
    ${UIDevice.currentDevice.systemVersion}
""".trimIndent()

object IODispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) { block.run() }
    }
}

object MainDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) { block.run() }
    }
}

actual fun ioDispatcher(): CoroutineDispatcher {
    return IODispatcher
}

actual fun mainDispatcher(): CoroutineDispatcher {
    return MainDispatcher
}

actual class Sender actual constructor(val host: String, val port: Int) {
    actual var onMessage: (suspend (Message) -> Unit)? = null
    actual var onReady: (suspend () -> Unit)? = null
    val json = Json(JsonConfiguration.Stable)

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    actual fun setup() {
        GlobalScope.launch(ioDispatcher()) {
            onReady?.invoke()
            var received = 0
            while (true) {
                val res = client.get<Array<Message>>("http://$host:$port/msg?offset=$received")
                res.forEach {
                    onMessage?.invoke(it)
                }
                received += res.size
            }
        }
    }

    actual fun send(msg: Message) {
        GlobalScope.launch(ioDispatcher()) {
            client.post<String>("http://$host:$port/msg") {
                contentType(ContentType.Application.Json)
                body = msg
            }
        }
    }
}