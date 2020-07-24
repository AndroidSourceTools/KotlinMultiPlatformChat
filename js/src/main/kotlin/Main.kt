import space.siy.kotlinmultiplatformchat.createApplicationScreenMessage
import space.siy.kotlinmultiplatformchat.fetchRemoteMessage
import kotlin.browser.document

fun main() {
    document.writeln(createApplicationScreenMessage())
    fetchRemoteMessage {
        document.writeln(it)
    }
}