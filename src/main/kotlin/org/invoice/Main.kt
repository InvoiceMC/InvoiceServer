package org.invoice

import net.minestom.server.MinecraftServer

lateinit var server: InvoiceServer

fun main() {
    val minecraftServer = MinecraftServer.init()
    server = InvoiceServer(minecraftServer)
    server.start()

    val events = Events(server.instanceContainer)
    events.init(server.eventHandler)
}