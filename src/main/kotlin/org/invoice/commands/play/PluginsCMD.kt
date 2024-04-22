package org.invoice.commands.play

import net.minestom.server.command.builder.Command
import org.invoice.mm
import org.invoice.server

class PluginsCMD : Command("plugins", "plugin", "pl") {
    init {
        setDefaultExecutor { sender, _ ->
            val pluginManager = server.pluginManager

            val enabledPlugins = pluginManager.loadedPlugins
            val disabledPlugins = pluginManager.disabledPlugins

            val formattedEnabledPlugins =
                enabledPlugins.joinToString(separator = "<white>, ", prefix = "<green>") { it.name }
            val formattedDisabledPlugins = disabledPlugins
                .joinToString(separator = "<white>, ", prefix = "<red>") { it.name }

            sender.sendMessage("<white>Enabled Plugins: $formattedEnabledPlugins".mm())
            sender.sendMessage("<white>Disabled Plugins: $formattedDisabledPlugins".mm())
        }
    }
}