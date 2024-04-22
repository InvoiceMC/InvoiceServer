package org.invoice

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandManager
import net.minestom.server.command.builder.Command
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.Block
import net.minestom.server.network.packet.server.play.TeamsPacket
import net.minestom.server.scoreboard.TeamManager
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import org.invoice.commands.admin.GamemodeCMD
import org.invoice.commands.admin.PickaxeCMD
import org.invoice.commands.play.PluginsCMD
import org.invoice.commands.play.TestingCMD
import org.invoice.plugins.PluginManager
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class InvoiceServer(private val minecraftServer: MinecraftServer) {
    var instanceManager: InstanceManager = MinecraftServer.getInstanceManager()
    var eventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()
    val teamManager: TeamManager = MinecraftServer.getTeamManager()

    val gson: Gson = Gson()
    val pluginManager: PluginManager = PluginManager()
    val performanceManager: InvoicePerformance = InvoicePerformance()

    var instanceContainer: InstanceContainer

    private val chunkSavingThread: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        MinecraftServer.setBrandName("InvoiceMC (Minestom)")
        MojangAuth.init()

        val dimension = DimensionType.builder(NamespaceID.from("invoice:main"))
            .ambientLight(1f)
            .build()

        MinecraftServer.getDimensionTypeManager().addDimension(dimension)
        instanceContainer = instanceManager.createInstanceContainer(dimension)
        performanceManager.setup(this, true)

        chunkSavingThread.scheduleAtFixedRate({ instanceContainer.saveChunksToStorage() }, 20, 20, TimeUnit.SECONDS)

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }

        setupCommands()
        setupTeams()
    }

    private fun setupCommands() {
        val commandManager = MinecraftServer.getCommandManager()

        commandManager.registerAll(
            // Admin
            GamemodeCMD(),
            PickaxeCMD(),

            // Play
            PluginsCMD(),
            TestingCMD()
        )
    }

    private fun setupTeams() {
        teamManager.createBuilder("players")
            .collisionRule(TeamsPacket.CollisionRule.NEVER)
            .nameTagVisibility(TeamsPacket.NameTagVisibility.NEVER)
            .build()
    }

    fun getResource(resource: String): String {
        val streamResource = getResourceAsStream(resource) ?: return ""
        return streamResource.reader().readText()
    }

    fun getResourceAsStream(resource: String): InputStream? = javaClass.getResourceAsStream("/$resource")
    fun getResourceAsJson(resource: String): JsonObject = gson.fromJson(getResource(resource), JsonObject::class.java)

    fun start() {
        minecraftServer.start("0.0.0.0", 25565)
    }

    private fun CommandManager.registerAll(vararg commands: Command) {
        for (command in commands) { register(command) }
    }
}