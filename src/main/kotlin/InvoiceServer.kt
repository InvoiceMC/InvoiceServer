package net.invoice

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class InvoiceServer(val minecraftServer: MinecraftServer) {
    var instanceManager: InstanceManager = MinecraftServer.getInstanceManager()
    var eventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()
    var instanceContainer: InstanceContainer

    private val chunkSavingThread: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        MojangAuth.init()

        val dimension = DimensionType.builder(NamespaceID.from("invoice:main"))
            .ambientLight(1f)
            .build()

        MinecraftServer.getDimensionTypeManager().addDimension(dimension)
        instanceContainer = instanceManager.createInstanceContainer(dimension)

        chunkSavingThread.scheduleAtFixedRate({ instanceContainer.saveChunksToStorage() }, 20, 20, TimeUnit.SECONDS)

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }
    }

    fun setupJoin() {
        eventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            val player = event.player
            event.spawningInstance = instanceContainer

            player.respawnPoint = Pos(0.0, 42.0, 0.0)
            player.gameMode = GameMode.CREATIVE // Just for Testing
        }
    }

    fun start() {
        minecraftServer.start("0.0.0.0", 25565)
    }
}