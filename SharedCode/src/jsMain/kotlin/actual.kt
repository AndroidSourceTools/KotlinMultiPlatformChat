/**
 * @author SIY1121
 */
package space.siy.kotlinmultiplatformchat

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.browser.window

actual fun platformName() = window.navigator.userAgent

actual fun ioDispatcher(): CoroutineDispatcher {
    return Dispatchers.Main
}

actual fun mainDispatcher(): CoroutineDispatcher {
    return Dispatchers.Main
}