package org.invoice.events

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.server.ServerListPingEvent
import org.invoice.mm
import org.invoice.server


class Events {
    private val playerNode = EventNode.all("player")
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
            player.team = server.teamManager.getTeam("players")
        }

    init {
        server.eventHandler.addChild(playerNode)
    }
}