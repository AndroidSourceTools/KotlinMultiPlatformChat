/**
 * @author SIY1121
 */

package space.siy.kotlinmultiplatformchat

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun platformName() = "Android"

actual fun ioDispatcher() = Dispatchers.IO
actual fun mainDispatcher(): CoroutineDispatcher {
    return Dispatchers.Main
}