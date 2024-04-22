package org.invoice.plugins

import java.io.File
import java.io.IOException

class PluginManager {
    internal val loadedPlugins: MutableList<Plugin> = ArrayList()
    internal val disabledPlugins: MutableList<Plugin> = ArrayList()

    @Throws(IOException::class)
    internal fun registerAllPlugins() {
        val files = File("plugins").listFiles()
            ?.filter { file -> !file.isDirectory || file.endsWith(".jar") }

        files?.let {
            it.forEach { file ->
                val plugin = loadPlugin(file)
                println("Registered plugin: ${plugin?.name}!")
            }
        }
    }

    @Throws(IOException::class)
    @Suppress("UNCHECKED_CAST")
    private fun loadPlugin(file: File): Plugin? {
        val classLoader = PluginClassLoader.create(file)
        val plugin = classLoader.registerAllClasses()
            .first { clazz -> clazz.javaClass.isInstance(Plugin::class.java) } as Class<out Plugin> // Had to do this because List#filterIsInstance didn't work for some reason :shrug:=
        val pluginInstance = plugin.getConstructor().newInstance() // Which means I have to do this :(

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