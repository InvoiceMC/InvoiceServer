package org.invoice.commands.play

import net.minestom.server.command.builder.Command
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta
import org.invoice.display.effects.BouncingDisplayEffect
import org.invoice.entities.HologramEntity
import org.invoice.mm
import org.invoice.server

class TestingCMD : Command("testing") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                val pos = sender.position
                val hologramEntity = HologramEntity("<dark_gray>⬇".mm(), Vec(7.5)) { meta ->
                    meta.billboardRenderConstraints = AbstractDisplayMeta.BillboardConstraints.VERTICAL
                }
                hologramEntity.setInstance(server.instanceContainer, pos)

                val effect = BouncingDisplayEffect(hologramEntity, 15, 0.5)
                effect.play()
            }
        }
    }
}