package org.invoice.plugins

import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import java.io.InputStream

open class Plugin {
    lateinit var info: PluginTOML
    lateinit var loader: PluginClassLoader
    val logger: ComponentLogger by lazy { ComponentLogger.logger(info.name) }

    open fun onEnable() {}
    open fun onDisable() {}

    fun getResource(resource: String): InputStream? = javaClass.getResourceAsStream(resource)
}