/**
 * @author SIY1121
 */

package space.siy.kotlinmultiplatformchat

expect fun platformName(): String

fun createApplicationScreenMessage() = "Kotlin Rocks on ${platformName()}"