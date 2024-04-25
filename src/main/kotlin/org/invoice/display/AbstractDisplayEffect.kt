package org.invoice.display

import net.minestom.server.entity.metadata.display.TextDisplayMeta
import org.invoice.entities.HologramEntity

/**
 * An abstract class for all display effects.
 *
 * @see DisplayEffect
 * @since 0.0.1
 * @author Outspending
 */
abstract class AbstractDisplayEffect @JvmOverloads constructor(entity: HologramEntity, duration: Int = 5) : DisplayEffect {
    init {
        entity.editEntityMeta(TextDisplayMeta::class.java) { meta ->
            meta.posRotInterpolationDuration = duration
        }
    }
}