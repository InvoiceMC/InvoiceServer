package org.invoice

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec

private val miniMessage: MiniMessage = MiniMessage.miniMessage()

fun String.mm(): Component = miniMessage.deserialize(this)
fun String.mm(vararg tags: TagResolver) = miniMessage.deserialize(this, *tags)

fun Vec.reverse(): Vec = Vec(-x, -y, -z)
fun Vec.toPos(): Pos = Pos(x, y, z)