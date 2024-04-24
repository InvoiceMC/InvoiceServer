package org.invoice

import net.minestom.server.MinecraftServer
import java.io.IOException

lateinit var server: InvoiceServer

@Throws(IOException::class)
fun main() {
    // Create server object
    val minecraftServer = MinecraftServer.init()
    server = InvoiceServer(minecraftServer)

    // VelocityProxy.enable("kVcL0dfPl65u")

    // Register default Events
    Events()

    // Register all plugins
    server.pluginManager.registerAllPlugins()

    minecraftServer.start("0.0.0.0", 25565)
}