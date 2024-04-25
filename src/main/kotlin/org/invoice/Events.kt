package org.invoice

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.server.ServerListPingEvent
import org.jetbrains.annotations.ApiStatus.Internal

@Internal
internal class Events {
    init {
        MinecraftServer.getGlobalEventHandler().addChild(
            EventNode.all("player")
                .addListener(ServerListPingEvent::class.java) { event ->
                    val response = event.responseData

                    response.maxPlayer = 500
                    response.description = "<rainbow>InvoiceMC Minestom!!!!!!!".mm()
                }
                .addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
                    val player = event.player
                    event.spawningInstance = server.instanceContainer

                    player.respawnPoint = Pos(0.0, 42.0, 0.0)
                    player.gameMode = GameMode.CREATIVE

                    server.broadcast("<yellow>${player.username} has joined the server.".mm())
                }
                .addListener(PlayerDisconnectEvent::class.java) { event ->
                    val username = event.player.username
                    server.broadcast("<yellow>$username has left the server.".mm())
                }
        )
    }
}