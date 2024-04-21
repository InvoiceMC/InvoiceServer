package org.invoice

import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage

@PublishedApi
internal val miniMessage: MiniMessage = MiniMessage.miniMessage()

fun String.mm(): ComponentLike = miniMessage.deserialize(this)