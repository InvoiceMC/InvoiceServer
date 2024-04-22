package org.invoice

import net.minestom.server.MinecraftServer
import org.invoice.events.Events
import java.io.IOException

lateinit var server: InvoiceServer

@Throws(IOException::class)
fun main() {
    // Create server object
    val minecraftServer = MinecraftServer.init()
    server = InvoiceServer(minecraftServer)
    server.start()

    // Register all default events
    Events()

    // Register all plugins
    server.pluginManager.registerAllPlugins()
}