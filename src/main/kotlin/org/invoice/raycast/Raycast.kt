package org.invoice.raycast

import net.minestom.server.collision.BoundingBox
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.LivingEntity
import net.minestom.server.instance.block.Block
import org.invoice.server
import org.invoice.toPos
import kotlin.math.cos
import kotlin.math.sin

class Raycast(val location: Pos, val settings: RaycastSettings) {
    constructor(entity: LivingEntity, settings: RaycastSettings) : this(entity.position, settings)

    var collisionBlock: Block? = null
    var collisionBoundingBox: BoundingBox? = null

    fun castRay() {
        val rayStart = location.asVec()
        val rayEnd = rayStart.add(settings.direction.mul(settings.maxDistance))

        for (blockVec in rayPositions(rayStart, rayEnd)) {
            val block = instance.getBlock(blockVec)
            if (settings.canCollideWithBlock && !block.isAir) {
                collisionBlock = block
                break
            }

            if (settings.canCollideWithBlock) {
                for (entity in instance.entities) {
                    val boundingBox = entity.boundingBox
                    val intersects = boundingBox.boundingBoxRayIntersectionCheck(rayStart, settings.direction, blockVec.toPos())
                    if (intersects) {
                        collisionBoundingBox = boundingBox
                    }
                }
            }
        }
    }

    private fun rayPositions(start: Vec, end: Vec): Iterator<Vec> {
        val positions: MutableList<Vec> = mutableListOf()
        val direction = end.sub(start).normalize()

        var current = start
        val length = end.distance(start).toInt()
        for (i in 0..length) {
            positions.add(current)
            current = current.add(direction)
        }

        return positions.iterator()
    }

    private companion object {
        val instance by lazy { server.instanceContainer }

        fun getEntityDirection(entity: LivingEntity): Vec {
            val pos = entity.position
            val yawRadians = Math.toRadians(pos.yaw.toDouble())
            val pitchRadians = Math.toRadians(pos.pitch.toDouble())

            val xzProjection = cos(pitchRadians)
            val x = -xzProjection * sin(yawRadians)
            val y = -sin(pitchRadians)
            val z = xzProjection * cos(yawRadians)

            return Vec(x, y, z).normalize()
        }
    }
}