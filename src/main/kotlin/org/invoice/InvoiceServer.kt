package org.invoice

import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.MinestomAdventure
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction

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

    fun start() {
        minecraftServer.start("0.0.0.0", 25565)
    }
}