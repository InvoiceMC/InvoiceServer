package org.invoice.plugins

import java.io.File
import java.io.IOException

class PluginManager {
    internal val loadedPlugins: MutableList<Plugin> = ArrayList()
    internal val disabledPlugins: MutableList<Plugin> = ArrayList()

    @Throws(IOException::class)
    internal fun registerAllPlugins() {
        val files = File("plugins").listFiles()
            ?.filter { file -> !file.isDirectory || file.endsWith(".jar") } ?: throw IOException("No plugins found!")

        files.forEach { file ->
            val plugin = loadPlugin(file)
            println("Registered plugin: ${plugin.name}!")
        }
    }

    @Throws(IOException::class)
    private fun loadPlugin(file: File): Plugin {
        val classLoader = PluginClassLoader.create(file)
        val classes = classLoader.registerAllClasses()

        val plugin = classes.filterIsInstance<Plugin>().first()
        plugin.onEnable()

        return plugin
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