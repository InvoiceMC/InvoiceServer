package org.invoice.plugins

import org.jetbrains.annotations.ApiStatus.Internal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import kotlin.time.measureTime

class PluginManager {
    private val logger: Logger = LoggerFactory.getLogger(PluginManager::class.java)

    internal val loadedPlugins: MutableList<Plugin> = ArrayList()
    internal val disabledPlugins: MutableList<Plugin> = ArrayList()
    private val allPlugins: List<Plugin>
        get() = loadedPlugins + disabledPlugins

    @Internal
    @Throws(IOException::class)
    internal fun registerAllPlugins() {
        val files = File("plugins").listFiles()
            ?.filter { file -> !file.isDirectory || file.endsWith(".jar") }

        files?.let {
            it.forEach { file ->
                var plugin: Plugin?
                val time = measureTime {
                    plugin = loadPlugin(file)
                }

                if (plugin == null) {
                    logger.error("Failed to register plugin in $time")
                    return@forEach
                } else {
                    logger.info("Registered plugin `${plugin!!.info.name}` in $time")
                }
            }
        }
    }

    @Throws(IOException::class)
    @Suppress("UNCHECKED_CAST")
    private fun loadPlugin(file: File): Plugin? {
        val classLoader = PluginClassLoader.create(file)
        val info = classLoader.info
        val mainClass = info.mainClass
        val plugin = classLoader.registerAllClasses()
            .find { it.name == mainClass && it.superclass == Plugin::class.java } as Class<Plugin>?

        return if (plugin == null) {
            logger.error("Failed to find main class for plugin `${info.name}`")
            null
        } else {
            val pluginInstance = plugin.getConstructor().newInstance()
            pluginInstance.info = info

            pluginInstance.onEnable()
            loadedPlugins.add(pluginInstance)

            pluginInstance
        }
    }

    fun loadPlugin(plugin: Plugin) {
        if (disabledPlugins.contains(plugin)) {
            disabledPlugins.remove(plugin)
            loadedPlugins.add(plugin)

            plugin.onEnable()
        }
    }

    fun disablePlugin(plugin: Plugin): Boolean {
        if (loadedPlugins.contains(plugin)) {
            plugin.onDisable()

            loadedPlugins.remove(plugin)
            disabledPlugins.add(plugin)
            return true
        }

        return false
    }

    fun reloadPlugin(plugin: Plugin) {
        if (loadedPlugins.contains(plugin)) {
            plugin.onDisable()
            plugin.onEnable()
        }
    }

    fun getPlugin(name: String): Plugin? {
        return allPlugins.find { it.info.name == name }
    }

    fun isEnabled(plugin: Plugin): Boolean {
        return loadedPlugins.contains(plugin)
    }
}