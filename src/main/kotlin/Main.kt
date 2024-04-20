package net.invoice

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType

fun main() {
    val minecraftServer = MinecraftServer.init()
    val instanceManager = MinecraftServer.getInstanceManager()

    val dimension = DimensionType.builder(NamespaceID.from("invoice:main"))
        .ambientLight(1f)
        .build()

    MinecraftServer.getDimensionTypeManager().addDimension(dimension)
    val instanceContainer = instanceManager.createInstanceContainer(dimension)

    MojangAuth.init()

    instanceContainer.setGenerator { unit ->
        unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
    }

    val eventHandler = MinecraftServer.getGlobalEventHandler()
    eventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
        val player = event.player
        event.spawningInstance = instanceContainer

        player.respawnPoint = Pos(0.0, 42.0, 0.0)
        player.gameMode = GameMode.CREATIVE // Just for Testing
    }

    minecraftServer.start("0.0.0.0", 25565)
}