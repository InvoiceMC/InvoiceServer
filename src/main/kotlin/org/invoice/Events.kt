package org.invoice

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.server.ServerListPingEvent
import org.invoice.raycast.Raycast
import org.invoice.raycast.RaycastSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.measureTime

internal class Events {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Events::class.java)

        fun setup() {
            server.eventHandler.addChild(
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
                        player.team = server.teamManager.getTeam("players")

                        server.broadcast("<yellow>${player.username} has joined the server.".mm())
                    }
                    .addListener(PlayerDisconnectEvent::class.java) { event ->
                        val username = event.player.username
                        server.broadcast("<yellow>$username has left the server.".mm())
                    }
                    .addListener(PlayerMoveEvent::class.java) { event ->
                        val player = event.player
                        val raycast = Raycast(player, RaycastSettings(Raycast.getEntityDirection(player)))
                        val time = measureTime {
                            raycast.castRay()
                        }

                        raycast.collisionBlock?.let {
                            logger.info("$time || $it")
                        }
                    }
            )
        }
    }
}