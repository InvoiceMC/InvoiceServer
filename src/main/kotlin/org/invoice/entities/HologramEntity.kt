package org.invoice.entities

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import org.invoice.mm

class HologramEntity(private val text: Component, private val scale: Vec = Vec.ONE) : NonTickingEntity(EntityType.TEXT_DISPLAY) {
    constructor(text: String, scale: Vec = Vec.ONE) : this(text.mm(), scale)

    init {
        editEntityMeta(TextDisplayMeta::class.java) { meta ->
            meta.text = text
            meta.billboardRenderConstraints = AbstractDisplayMeta.BillboardConstraints.CENTER

            meta.scale = scale
            meta.isSeeThrough = true
            meta.backgroundColor = 0x0
        }
    }
}