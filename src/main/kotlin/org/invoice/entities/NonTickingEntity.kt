package org.invoice.entities

import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.Tickable

/**
 * Represents a class that doesn't execute any code once its ticked.
 *
 * I could make this better by actually making my own entity class without [Tickable] but I'm too lazy.
 *
 * @see HologramEntity
 * @since 0.0.1
 * @author Outspending
 */
open class NonTickingEntity(entityType: EntityType) : Entity(entityType) {
    override fun tick(time: Long) {}
}