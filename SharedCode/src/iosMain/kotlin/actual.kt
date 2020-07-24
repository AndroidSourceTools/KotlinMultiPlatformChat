package space.siy.kotlinmultiplatformchat

import kotlinx.coroutines.*
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