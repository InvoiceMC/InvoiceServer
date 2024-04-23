package org.invoice.plugins

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import kotlin.time.measureTime

class PluginManager {
    private val logger: Logger = LoggerFactory.getLogger(PluginManager::class.java)

    internal val loadedPlugins: MutableList<Plugin> = ArrayList()
    internal val disabledPlugins: MutableList<Plugin> = ArrayList()

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
            .find { it.superclass == Plugin::class.java && it.name == mainClass } as Class<Plugin>?
        if (plugin == null) {
            logger.error("Failed to find main class for plugin `${info.name}`")
            val possibleClasses = classLoader.registerAllClasses()
                .filter { it.superclass == Plugin::class.java }
                .map { it.name }
            logger.error("Possible classes: $possibleClasses")
            return null
        }
        val pluginInstance = plugin.getConstructor().newInstance() // Which means I have to do this :(
        pluginInstance.info = info

        pluginInstance.onEnable()
        loadedPlugins.add(pluginInstance)

        return pluginInstance
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
}