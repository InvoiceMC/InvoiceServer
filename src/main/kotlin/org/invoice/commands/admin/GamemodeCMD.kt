package org.invoice.commands.admin

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentEnum
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder
import org.invoice.mm

internal class GamemodeCMD : Command("gamemode", "gm") {
    init {
        val gamemode: ArgumentEnum<GameMode> = ArgumentType.Enum("gamemode", GameMode::class.java)
            .setFormat(ArgumentEnum.Format.LOWER_CASED)

        val player: ArgumentEntity = ArgumentType.Entity("target")
            .onlyPlayers(true)
            .singleEntity(true)

        setDefaultExecutor { sender, context ->
            val commandName: String = context.commandName
            sender.sendMessage("<red>Usage: /$commandName <gamemode> [target]".mm())
        }

        // Command: /gamemode <gamemode>
        addSyntax({ sender, context ->
            if (sender !is Player) {
                sender.sendMessage("<red>Please run this command in-game.".mm())
            } else {
                val gamemode: GameMode = context.get(gamemode);
                setGamemode(sender, gamemode)
            }
        }, gamemode)

        // Command: /gamemode <gamemode> [target]
        addSyntax({ sender, context ->
            if (sender !is Player) return@addSyntax

            val finder: EntityFinder = context.get(player)
            val gamemode: GameMode = context.get(gamemode)

            val foundPlayer = finder.findFirstPlayer(sender) ?: return@addSyntax
            setOtherGamemode(sender, foundPlayer, gamemode)
        }, gamemode, player)
    }

    private fun setOtherGamemode(executor: Player, player: Player, mode: GameMode) {
        player.setGameMode(mode)
        executor.sendMessage("<white>Set <yellow>${player.username}'s <white>gamemode to <yellow>${getName(mode)}<white>!".mm())
    }

    private fun setGamemode(player: Player, mode: GameMode) {
        player.setGameMode(mode)
        player.sendMessage("<white>Set your gamemode to <yellow>${getName(mode)}<white>!".mm())
    }

    private fun getName(gameMode: GameMode): String =
        gameMode.name.lowercase().replaceFirstChar { it.uppercase() }
}