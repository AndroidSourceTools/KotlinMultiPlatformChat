/**
 * @author SIY1121
 */

package space.siy.kotlinmultiplatformchat

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
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