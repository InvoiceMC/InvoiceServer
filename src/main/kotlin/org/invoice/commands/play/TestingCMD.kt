package org.invoice.commands.play

import net.minestom.server.command.builder.Command
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import org.invoice.entities.HologramEntity
import org.invoice.mm
import org.invoice.server

class TestingCMD : Command("testing") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                val pos = sender.position

                val hologram = HologramEntity("<rainbow>Imagine doing this".mm(), Vec(2.5)) { meta ->
                    meta.isAlignLeft = true
                }
                hologram.setInstance(server.instanceContainer, pos)
            }
        }
    }
}