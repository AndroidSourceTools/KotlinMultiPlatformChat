import react.RState
import space.siy.kotlinmultiplatformchat.ChatService
import space.siy.kotlinmultiplatformchat.createApplicationScreenMessage
import space.siy.kotlinmultiplatformchat.fetchRemoteMessage
import kotlin.browser.document
import react.dom.*
import space.siy.kotlinmultiplatformchat.Message



//@ImplicitReflectionSerializer
fun main() {
    render(document.getElementById("root")) {
        child(App::class) {}
    }
}