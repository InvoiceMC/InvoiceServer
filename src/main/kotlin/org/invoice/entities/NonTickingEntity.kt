package org.invoice

import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType

class NonTickingEntity(private val entityType: EntityType) : Entity(entityType) {
    override fun tick(time: Long) {}
    override fun update(time: Long) {}
}