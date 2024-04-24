package org.invoice.commands.play

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import org.invoice.mm
import org.invoice.server
import kotlin.time.measureTime

/**
 * Command to manage plugins
 * ```
 * /plugin <info|enable|disable|reload> <pluginName>
 */
class PluginsCMD : Command("plugins", "plugin", "pl") {
    private val pluginManager
        get() = server.pluginManager

    init {
        setDefaultExecutor { sender, context ->
            val enabledPlugins = pluginManager.loadedPlugins
            val disabledPlugins = pluginManager.disabledPlugins

            val formattedEnabledPlugins =
                enabledPlugins.joinToString(separator = "<white>, <green>", prefix = "<green>") { it.info.name }
            val formattedDisabledPlugins = disabledPlugins
                .joinToString(separator = "<white>, <red>", prefix = "<red>") { it.info.name }

            sender.sendMessage("<white>Enabled Plugins: $formattedEnabledPlugins".mm())
            sender.sendMessage("<white>Disabled Plugins: $formattedDisabledPlugins".mm())
        }

        val info = ArgumentType.Literal("info")
        val enable = ArgumentType.Literal("enable")
        val disable = ArgumentType.Literal("disable")
        val reload = ArgumentType.Literal("reload")

        val pluginName = ArgumentType.String("pluginName").apply {
            setSuggestionCallback { sender, context, suggestion ->
                val value = context.get<String>(this.id) ?: ""
                pluginManager.loadedPlugins
                    .map { it.info.name }
                    .filter { it.contains(value, ignoreCase = true) }
                    .forEach { suggestion.addEntry(
                        SuggestionEntry(it)
                    ) }
            }
        }
        val getPlugin = { context: CommandContext ->
            val name = context.get(pluginName)
            pluginManager.getPlugin(name)
        }

        addSyntax({ sender, context ->
            val plugin = getPlugin(context)
            if (plugin == null) {
                sender.sendMessage("<red>Plugin not found".mm())
                return@addSyntax
            } else {
                val enabled = pluginManager.isEnabled(plugin)
                val color = if (enabled) "<green>" else "<red>"
                val message = listOf(
                    "",
                    "$color${plugin.info.name} <white>(${plugin.info.version})",
                    "<white>By <green>${plugin.info.authors.joinToString()}",
                    "<white>${plugin.info.description}",
                    ""
                ).joinToString("<br>")
                sender.sendMessage(message.mm())
            }
        }, info, pluginName)

        addSyntax({ sender, context ->
            val plugin = getPlugin(context)
            if (plugin == null) {
                sender.sendMessage("<red>Plugin not found".mm())
                return@addSyntax
            } else {
                if (pluginManager.isEnabled(plugin)) {
                    sender.sendMessage("<red>Plugin is already enabled".mm())
                    return@addSyntax
                }

                val time = measureTime {
                    pluginManager.loadPlugin(plugin)
                }
                sender.sendMessage("<white>Plugin enabled in <green>$time".mm())
            }
        }, enable, pluginName)

        addSyntax({ sender, context ->
            val plugin = getPlugin(context)
            if (plugin == null) {
                sender.sendMessage("<red>Plugin not found".mm())
                return@addSyntax
            } else {
                if (!pluginManager.isEnabled(plugin)) {
                    sender.sendMessage("<red>Plugin is already disabled".mm())
                    return@addSyntax
                }

                val time = measureTime {
                    pluginManager.disablePlugin(plugin)
                }
                sender.sendMessage("<white>Plugin disabled in <green>$time".mm())
            }
        }, disable, pluginName)

        addSyntax({ sender, context ->
            val plugin = getPlugin(context)
            if (plugin == null) {
                sender.sendMessage("<red>Plugin not found".mm())
                return@addSyntax
            } else {
                if (!pluginManager.isEnabled(plugin)) {
                    sender.sendMessage("<red>Plugin is not enabled".mm())
                    return@addSyntax
                }
                val time = measureTime {
                    pluginManager.reloadPlugin(plugin)
                }
                sender.sendMessage("<white>Plugin reloaded in <green>$time".mm())
            }
        }, reload, pluginName)
    }
}