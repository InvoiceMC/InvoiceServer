package org.invoice.raycast

import net.minestom.server.coordinate.Vec

data class RaycastSettings @JvmOverloads constructor(
    val direction: Vec,
    val maxDistance: Double = 10.0,
    val canCollideWithBlock: Boolean = true,
    val canCollideWithBoundingBox: Boolean = false
)