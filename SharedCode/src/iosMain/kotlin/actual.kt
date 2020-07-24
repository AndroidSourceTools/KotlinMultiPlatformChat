package space.siy.kotlinmultiplatformchat
import platform.UIKit.UIDevice

/**
 * @author SIY1121
 */

actual fun platformName() = """
    ${UIDevice.currentDevice.systemName}
    ${UIDevice.currentDevice.systemVersion}
""".trimIndent()