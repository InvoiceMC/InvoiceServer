package org.invoice

import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.event.server.ServerTickMonitorEvent
import net.minestom.server.monitoring.TickMonitor
import net.minestom.server.utils.MathUtils
import net.minestom.server.utils.time.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.floor
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class InvoicePerformance {
    private val benchmarkFormat: String = "<gray>ʀᴀᴍ: <white>%sMB/%sMB (%s) <dark_gray><b>|<reset> <gray>ᴛᴘꜱ: <white>%s <dark_gray><b>|<reset> <gray>ᴛɪᴄᴋ ᴛɪᴍᴇ: <white>%s"
    private val lowTPSThreshold: Double = 15.5

    val MONITOR: AtomicReference<TickMonitor> = AtomicReference()

    var ramUsage: Long = 0
    var ramMax: Long = 0
    var ramPercentage: Float = 0f

    var tps: Double = 20.0
    var tickTime: Duration = Duration.ZERO

    internal fun setup(server: InvoiceServer, showMessage: Boolean) {
        server.eventHandler.addListener(ServerTickMonitorEvent::class.java) { event -> MONITOR.set(event.tickMonitor) }
        setupBenchmarker(showMessage)
    }

    private fun setupBenchmarker(showMessage: Boolean = false) {
        val benchmarkManager = MinecraftServer.getBenchmarkManager()
        val scheduler = MinecraftServer.getSchedulerManager()
        val runTime = Runtime.getRuntime()

        scheduler.buildTask {
            ramUsage = (benchmarkManager.usedMemory / 1e6).toLong()
            ramMax = (runTime.maxMemory() / 1e6).toLong()
            ramPercentage = ramUsage.toFloat() / ramMax.toFloat() * 100f

            MONITOR.get()?.let {
                tps = min(MinecraftServer.TICK_PER_SECOND.toDouble(), floor(1000.0 / it.tickTime))
                tickTime = MathUtils.round(it.tickTime, 2)
                    .toDuration(DurationUnit.MILLISECONDS)

                if (showMessage) {
                    Audiences.players().sendActionBar(getServerPerformance())
                }
            }
        }.repeat(10, TimeUnit.SERVER_TICK).schedule()
    }

    fun getRamPercentage(): String = "${"%.2f".format(ramPercentage)}%"
    fun getServerPerformance(): Component = benchmarkFormat.format(ramUsage, ramMax, getRamPercentage(), tps, tickTime).mm()
    fun isLowTPS(): Boolean = tps <= lowTPSThreshold
}