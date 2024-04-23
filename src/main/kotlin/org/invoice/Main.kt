package org.invoice

import net.minestom.server.MinecraftServer
import net.minestom.server.extras.velocity.VelocityProxy
import java.io.IOException

lateinit var server: InvoiceServer

@Throws(IOException::class)
fun main() {
    // Create server object
    val minecraftServer = MinecraftServer.init()
    server = InvoiceServer(minecraftServer)
    server.start()

    VelocityProxy.enable("secret")

    // Register all default events
    Events.setup()

    // Register all plugins
    server.pluginManager.registerAllPlugins()
}