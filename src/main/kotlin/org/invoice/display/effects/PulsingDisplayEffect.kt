package org.invoice.display.effects

import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import org.invoice.display.KeyframeDisplayEffect
import org.invoice.entities.HologramEntity
import org.invoice.reverse

/**
 * This effect will make the [HologramEntity] scale up and down.
 *
 * @see HologramEntity
 * @see KeyframeDisplayEffect
 * @since 0.0.1
 * @author Outspending
 */
class PulsingDisplayEffect(
    private val hologramEntity: HologramEntity,
    duration: Int,
    private val minMaxScale: Vec
) : KeyframeDisplayEffect(hologramEntity, duration) {
    private var isScalingUp = true

    override fun keyframe(keyframe: Int) {
        val deltaScale = if (isScalingUp) minMaxScale else minMaxScale.reverse()

        hologramEntity.editEntityMeta(TextDisplayMeta::class.java) { meta -> meta.scale = deltaScale }
        isScalingUp = !isScalingUp
    }
}