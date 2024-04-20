package net.invoice

import net.minestom.server.MinecraftServer

fun main() {
    val minecraftServer = MinecraftServer.init()
    val server = InvoiceServer(minecraftServer)

    server.setupJoin()
    server.start()
}