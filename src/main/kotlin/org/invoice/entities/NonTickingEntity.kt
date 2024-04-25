package org.invoice.entities

import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType

open class NonTickingEntity(entityType: EntityType) : Entity(entityType) {
    override fun tick(time: Long) {}
}