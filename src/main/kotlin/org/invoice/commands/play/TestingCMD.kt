package org.invoice.commands.play

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.instance.block.Block
import org.invoice.blocks.BlockSelection

class TestingCMD : Command("testing") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                val vecPos = sender.position.asVec()

//                val hologram = HologramEntity("<rainbow>Imagine doing this", Vec(2.5))
//                hologram.setInstance(server.instanceContainer, pos)

                val minPos = vecPos.sub(10.0, 10.0, 10.0)
                val maxPos = vecPos.add(10.0, 10.0, 10.0)

                val selection = BlockSelection(minPos, maxPos)
                selection.setBlocks(Block.COBBLESTONE)
            }
        }
    }
}