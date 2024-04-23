package org.invoice

import net.minestom.server.MinecraftServer
import net.minestom.server.extras.velocity.VelocityProxy
import java.io.IOException

lateinit var server: InvoiceServer

@Throws(IOException::class)
fun main(args: Array<String>) {
    // Create server object
    val minecraftServer = MinecraftServer.init()
    server = InvoiceServer(minecraftServer)
    server.start()


    val velocitySecret = args.getOrNull(0)
    if (velocitySecret == null) {
        println("Velocity secret not found. Not enabling Velocity proxy.")
    } else {
        println("Velocity secret found. Enabling Velocity proxy.")
        VelocityProxy.enable(velocitySecret)
    }

    // Register all default events
    Events.setup()

    // Register all plugins
    server.pluginManager.registerAllPlugins()
}