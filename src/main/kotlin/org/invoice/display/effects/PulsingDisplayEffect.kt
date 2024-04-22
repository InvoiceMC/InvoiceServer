package org.invoice.display.effects

import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import org.invoice.display.KeyframeDisplayEffect
import org.invoice.entities.HologramEntity
import org.invoice.reverse

class PulsingDisplayEffect(private val hologramEntity: HologramEntity, duration: Int, private val minMaxScale: Vec) : KeyframeDisplayEffect(hologramEntity, duration) {
    private var isScalingUp = true

    override fun keyframe(keyframe: Int) {
        hologramEntity.editEntityMeta(TextDisplayMeta::class.java) { meta ->
            meta.scale = if (isScalingUp) minMaxScale else minMaxScale.reverse()
        }
        isScalingUp = !isScalingUp
    }
}