package org.invoice.plugins

import com.google.gson.JsonObject
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.invoice.server
import java.io.InputStream

open class Plugin {
    lateinit var info: PluginTOML
    val logger: ComponentLogger by lazy { ComponentLogger.logger(info.name) }

    open fun onEnable() {}
    open fun onDisable() {}

    fun getResourceAsStream(resource: String): InputStream? = server.getResourceAsStream(resource)
    fun getResourceAsString(resource: String): String = server.getResourceAsString(resource)
    fun getResourceAsJson(resource: String): JsonObject = server.getResourceAsJson(resource)
}