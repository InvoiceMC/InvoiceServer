package org.invoice

import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.ServerFlag
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

@Suppress("MemberVisibilityCanBePrivate")
class InvoicePerformance {
    private val benchmarkFormat: String = "<gray>ʀᴀᴍ: <white>%sMB/%sMB <dark_gray><b>(</b><white>%s<dark_gray><b>) <dark_gray><b>|<reset> <gray>ᴛᴘꜱ: <white>%s <dark_gray><b>|<reset> <gray>ᴛɪᴄᴋ ᴛɪᴍᴇ: <white>%s <dark_gray><b>(</b><gray>ᴍꜱᴘᴛ: <white>%s<dark_gray><b>)"
    private val lowTPSThreshold: Double = 15.5

    val tickMonitor: AtomicReference<TickMonitor> = AtomicReference()

    var ramUsage: Long = 0
    var ramMax: Long = 0

    var tps: Double = 20.0
    var tickTime: Double = 0.0

    internal fun setup(server: InvoiceServer, showMessage: Boolean) {
        MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent::class.java) { event -> tickMonitor.set(event.tickMonitor) }
        setupBenchmark(showMessage)
    }

    private fun setupBenchmark(showMessage: Boolean = false) {
        val benchmarkManager = MinecraftServer.getBenchmarkManager()
        val scheduler = MinecraftServer.getSchedulerManager()
        val runTime = Runtime.getRuntime()

        scheduler.buildTask {
            ramUsage = (benchmarkManager.usedMemory / 1e6).toLong()
            ramMax = (runTime.maxMemory() / 1e6).toLong()

            tickMonitor.get()?.let {
                tps = min(ServerFlag.SERVER_TICKS_PER_SECOND.toDouble(), floor(1000.0 / it.tickTime))
                tickTime = MathUtils.round(it.tickTime, 2)

                if (showMessage) {
                    Audiences.players().sendActionBar(getServerPerformance())
                }
            }
        }.repeat(10, TimeUnit.SERVER_TICK).schedule()
    }

    fun getTickTimeDuration(): Duration = tickTime.toDuration(DurationUnit.MILLISECONDS)
    fun getRamPercentage(): Float = ramUsage.toFloat() / ramMax.toFloat() * 100f
    fun getServerPerformance(): Component = benchmarkFormat.format(ramUsage, ramMax, "${"%.2f".format(getRamPercentage())}%", tps, getTickTimeDuration(), tickTime).mm()
    fun isLowTPS(): Boolean = tps <= lowTPSThreshold
}