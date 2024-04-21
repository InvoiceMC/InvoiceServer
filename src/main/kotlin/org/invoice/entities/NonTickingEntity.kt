package org.invoice.entities

import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType

open class NonTickingEntity(private val entityType: EntityType) : Entity(entityType) {
    override fun tick(time: Long) {}
    override fun update(time: Long) {}
}