package org.invoice.plugins

import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import java.io.InputStream

open class Plugin @JvmOverloads constructor(val name: String, val description: String = "", val version: String = "0.0.1", val authors: Array<out String> = arrayOf()) {
    val logger: ComponentLogger = ComponentLogger.logger(name)

    open fun onEnable() {}
    open fun onDisable() {}

    fun getResource(resource: String): InputStream? = javaClass.getResourceAsStream(resource)
}