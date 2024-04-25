package org.invoice.display.effects

import org.invoice.display.KeyframeDisplayEffect
import org.invoice.entities.HologramEntity

class BouncingDisplayEffect(
    private val hologramEntity: HologramEntity,
    duration: Int = 5,
    private val minMaxY: Double = 1.0
) : KeyframeDisplayEffect(hologramEntity, duration) {
    private val centerPos = hologramEntity.position
    private var isUpward: Boolean = true

    override fun keyframe(keyframe: Int) {
        val deltaY = if (isUpward) minMaxY else -minMaxY

        hologramEntity.teleport(centerPos.add(0.0, deltaY, 0.0))
        isUpward = !isUpward
    }
}