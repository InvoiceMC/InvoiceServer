package org.invoice.events

import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import net.minestom.server.event.EventNode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.instance.InstanceTickEvent
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerPacketEvent
import net.minestom.server.event.player.PlayerStartDiggingEvent
import net.minestom.server.event.server.ServerListPingEvent
import net.minestom.server.event.server.ServerTickMonitorEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.monitoring.TickMonitor
import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.utils.MathUtils
import net.minestom.server.utils.time.TimeUnit
import org.invoice.entities.HologramEntity
import org.invoice.entities.NonTickingEntity
import org.invoice.mm
import org.invoice.server
import org.w3c.dom.Text
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.floor
import kotlin.math.min
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class Events(instanceContainer: InstanceContainer) {
    private val LAST_TICK: AtomicReference<TickMonitor> = AtomicReference()
    private val playerNode = EventNode.all("player")
        .addListener(ServerListPingEvent::class.java) { event ->
            val response = event.responseData

            response.maxPlayer = 500
            response.description = "<rainbow>InvoiceMC Minestom!!!!!!!".mm()
        }
        .addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            val player = event.player
            event.spawningInstance = instanceContainer

            player.respawnPoint = Pos(0.0, 42.0, 0.0)
            player.gameMode = GameMode.CREATIVE
            player.team = server.teamManager.getTeam("players")
        }

    fun init(eventHandler: GlobalEventHandler) {
        eventHandler.addListener(ServerTickMonitorEvent::class.java) { event -> LAST_TICK.set(event.tickMonitor) }
        eventHandler.addChild(playerNode)

        val benchmarkManager = MinecraftServer.getBenchmarkManager()
        MinecraftServer.getSchedulerManager().buildTask {
            if (MinecraftServer.getConnectionManager().onlinePlayerCount == 0) {
                return@buildTask;
            } else {
                val ramUsage: Long = (benchmarkManager.usedMemory / 1e6).toLong()
                val ramMax: Long = (Runtime.getRuntime().maxMemory() / 1e6).toLong()

                val ramPercentage = ramUsage.toFloat() / ramMax.toFloat() * 100f
                val formattedPercentage = "%.2f".format(ramPercentage)

                val monitor = LAST_TICK.get()
                val tps = min(MinecraftServer.TICK_PER_SECOND.toDouble(), floor(1000.0 / monitor.tickTime))
                val tickTime = MathUtils.round(monitor.tickTime, 2)
                    .toDuration(DurationUnit.MILLISECONDS)

                Audiences.players().sendActionBar("<gray>ʀᴀᴍ: <white>${ramUsage}MB/${ramMax}MB ($formattedPercentage%) <dark_gray><b>|<reset> <gray>ᴛᴘꜱ: <white>$tps <dark_gray><b>|<reset> <gray>ᴛɪᴄᴋ ᴛɪᴍᴇ: <white>$tickTime".mm())
            }
        }.repeat(10, TimeUnit.SERVER_TICK).schedule()
    }
}