package org.invoice.display

import net.minestom.server.entity.metadata.display.TextDisplayMeta
import org.invoice.entities.HologramEntity

abstract class AbstractDisplayEffect @JvmOverloads constructor(entity: HologramEntity, duration: Int = 5) : DisplayEffect {
    init {
        entity.editEntityMeta(TextDisplayMeta::class.java) { meta ->
            meta.posRotInterpolationDuration = duration
        }
    }
}