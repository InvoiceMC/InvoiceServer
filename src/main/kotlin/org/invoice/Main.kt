package org.invoice

import net.minestom.server.MinecraftServer
import org.invoice.events.Events
import java.io.IOException

lateinit var server: InvoiceServer

@Throws(IOException::class)
fun main() {
    val minecraftServer = MinecraftServer.init()
    server = InvoiceServer(minecraftServer)
    server.start()

    val events = Events(server.instanceContainer)
    events.init(server.eventHandler)

    server.pluginManager.registerAllPlugins()
}