package org.invoice

import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventNode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.server.ServerTickMonitorEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.monitoring.TickMonitor
import net.minestom.server.utils.MathUtils
import net.minestom.server.utils.time.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class Events(instanceContainer: InstanceContainer) {
    val LAST_TICK: AtomicReference<TickMonitor> = AtomicReference()
    private val BREAKABLE_BLOCKS: List<Block> = listOf(
        Block.COBBLESTONE,
        Block.COAL_ORE
    )

    private val playerNode = EventNode.all("player")
        .addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            val player = event.player
            event.spawningInstance = instanceContainer

            player.respawnPoint = Pos(0.0, 42.0, 0.0)
            player.gameMode = GameMode.CREATIVE
        }
        .addListener(PlayerBlockBreakEvent::class.java) { event ->
            val block = event.block
            if (BREAKABLE_BLOCKS.contains(block)) {
                Audiences.all().sendMessage("<rainbow>YES!".mm())
            }
        }

    fun init(eventHandler: GlobalEventHandler) {
        eventHandler.addListener(ServerTickMonitorEvent::class.java) { event -> LAST_TICK.set(event.tickMonitor) }
        eventHandler.addChild(playerNode)

        val benchmarkManager = MinecraftServer.getBenchmarkManager()
        MinecraftServer.getSchedulerManager().buildTask {
            if (MinecraftServer.getConnectionManager().onlinePlayerCount == 0)
                return@buildTask;

            var ramUsage: Long = benchmarkManager.usedMemory
            ramUsage /= 1e6.toLong()

            val monitor = LAST_TICK.get()
            val tickTime = MathUtils.round(monitor.tickTime, 2)
                .toDuration(DurationUnit.MILLISECONDS)
            val tickAcquisition = MathUtils.round(monitor.acquisitionTime, 2)
                .toDuration(DurationUnit.MILLISECONDS)


            Audiences.players().sendActionBar("<gray>ʀᴀᴍ: <white>${ramUsage}MB <dark_gray><b>|<reset> <gray>ᴛɪᴄᴋ ᴛɪᴍᴇ: <white>$tickTime <dark_gray><b>|<reset> <gray>ᴛɪᴄᴋ ᴀᴄQ: <white>$tickAcquisition".mm())
        }.repeat(10, TimeUnit.SERVER_TICK).schedule()
    }
}