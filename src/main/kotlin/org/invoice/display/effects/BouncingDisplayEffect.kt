package org.invoice.display.effects

import org.invoice.display.KeyframeDisplayEffect
import org.invoice.entities.HologramEntity

/**
 * Gives a [HologramEntity] a bouncing effect, moving it up and down.
 *
 * The translation of the Display Entity will give it a smooth effect.
 *
 * @see HologramEntity
 * @see KeyframeDisplayEffect
 * @since 0.0.1
 * @author Outspending
 */
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