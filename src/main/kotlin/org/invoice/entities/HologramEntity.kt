package org.invoice.entities

import net.kyori.adventure.text.Component
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import org.invoice.mm

class HologramEntity(private val text: Component) : NonTickingEntity(EntityType.TEXT_DISPLAY) {
    constructor(text: String) : this(text.mm())

    init {
        editEntityMeta(TextDisplayMeta::class.java) { meta ->
            meta.text = text
            meta.billboardRenderConstraints = AbstractDisplayMeta.BillboardConstraints.CENTER

            meta.isSeeThrough = true
            meta.backgroundColor = 0x0
        }
    }
}