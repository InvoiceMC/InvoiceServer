package org.invoice.entities

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta.BillboardConstraints
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import org.invoice.display.effects.PulsingDisplayEffect
import org.invoice.display.effects.BouncingDisplayEffect

/**
 * This class represents a hologram entity.
 * Its a [EntityType.TEXT_DISPLAY] entity that has no background.
 *
 * This class is required for creating display effects such as [BouncingDisplayEffect] and [PulsingDisplayEffect].
 *
 * @see BouncingDisplayEffect
 * @see PulsingDisplayEffect
 * @since 0.0.1
 * @author Outspending
 */
class HologramEntity @JvmOverloads constructor(
    text: Component,
    scale: Vec = Vec.ONE,
    billboardConstraint: BillboardConstraints = BillboardConstraints.CENTER,
    metaFunction: (TextDisplayMeta) -> Unit = {}
) : NonTickingEntity(EntityType.TEXT_DISPLAY) {
    init {
        editEntityMeta(TextDisplayMeta::class.java) { meta ->
            meta.text = text
            meta.billboardRenderConstraints = billboardConstraint

            meta.scale = scale
            meta.isSeeThrough = true
            meta.backgroundColor = 0x0

            metaFunction(meta)
        }
    }
}