package org.invoice.commands.admin

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.Player
import net.minestom.server.item.Enchantment
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import org.invoice.mm

internal class PickaxeCMD : Command("pickaxe") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender !is Player) return@setDefaultExecutor

            createPickaxe(sender)
        }

        val player: ArgumentEntity = ArgumentType.Entity("player")
            .onlyPlayers(true)
            .singleEntity(true)

        // Command: /pickaxe [player]
        addSyntax({ sender, context ->
            if (sender !is Player) return@addSyntax

            val playerContext = context.get(player)
            val firstFind = playerContext.findFirstPlayer(sender) ?: return@addSyntax

            createPickaxe(firstFind)
        }, player)
    }

    private fun createPickaxe(player: Player) {
        val item: ItemStack = ItemStack.builder(Material.DIAMOND_PICKAXE)
            .displayName("<yellow>${player.username}'s <gray>Pickaxe".mm())
            .meta { meta ->
                meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS)
                meta.enchantment(Enchantment.EFFICIENCY, 500)
            }
            .build()

        player.inventory.addItemStack(item)
    }
}