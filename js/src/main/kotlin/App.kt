import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.onClick
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import space.siy.kotlinmultiplatformchat.ChatService
import space.siy.kotlinmultiplatformchat.Message
import kotlin.browser.document

external interface AppState : RState {
    var messages: List<Message>
    var fieldText: String
}

class App : RComponent<RProps, AppState>() {
    lateinit var chat: ChatService

    override fun AppState.init() {
        messages = emptyList()
        fieldText = ""
    }

    override fun componentDidMount() {
        chat = ChatService("192.168.2.127", 8081, "BrowserUser")
        chat.onMessage = {
            setState {
                messages += it
            }
        }
        chat.onReady = {
            chat.send("join")
        }
        chat.connect()
    }

    override fun RBuilder.render() {
        h1 {
            +"Kotlin MultiPlatform Chat"
        }
        ul {
            state.messages.forEach {
                li {
                    +"${it.user}: ${it.content}"
                }
            }
        }
        input (InputType.text){
            attrs {
                onChangeFunction = {
                    val target = (it.target as HTMLInputElement)
                    setState {
                        fieldText = target.value
                    }
                }
            }
        }
        button {
            attrs {
                onClickFunction = {
                    println("click")
                    setState {
                        fieldText = ""
                    }
                    chat.send(state.fieldText)
                }
            }
            +"send"
        }
    }
}