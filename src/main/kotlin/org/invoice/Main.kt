package org.invoice

import net.minestom.server.MinecraftServer
import net.minestom.server.collision.BoundingBox
import org.invoice.events.Events
import org.invoice.plugins.PluginManager
import java.io.IOException
import kotlin.jvm.Throws

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