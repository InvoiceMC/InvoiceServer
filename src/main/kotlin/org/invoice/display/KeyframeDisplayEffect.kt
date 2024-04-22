package org.invoice.display

import net.minestom.server.MinecraftServer
import net.minestom.server.timer.Task
import net.minestom.server.utils.time.TimeUnit
import org.invoice.entities.HologramEntity

abstract class KeyframeDisplayEffect(private val hologramEntity: HologramEntity, private val duration: Int = 5) : AbstractDisplayEffect(hologramEntity, duration) {
    lateinit var task: Task
    var keyframe: Int = 0

    override fun stop() = task.cancel()
    override fun play() {
        task = MinecraftServer.getSchedulerManager().buildTask {
            keyframe(++keyframe)
        }.repeat(duration.toLong(), TimeUnit.SERVER_TICK).schedule()
    }

    abstract fun keyframe(keyframe: Int)
}