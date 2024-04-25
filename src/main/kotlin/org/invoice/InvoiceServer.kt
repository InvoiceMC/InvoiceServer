package org.invoice

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.command.CommandManager
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.function.Predicate

internal class InvoiceServer {
    private val logger: Logger = LoggerFactory.getLogger(MinecraftServer::class.java)
    private val performanceManager: InvoicePerformance = InvoicePerformance()
    private val gson: Gson = Gson()

    val pluginManager: PluginManager = PluginManager()
    var instanceContainer: InstanceContainer

    private val chunkSavingThread: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        // Setup brandName and turn Online Mode on
        MinecraftServer.setBrandName("InvoiceMC (Minestom)")
        MojangAuth.init()

        // Set up the dimension (for ambient light)
        val dimension = DimensionType.builder(NamespaceID.from("invoice:main"))
            .ambientLight(1f)
            .build()
        MinecraftServer.getDimensionTypeManager().addDimension(dimension)

        // Set the instance to a new instance with the created dimension
        instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(dimension)

        // Set up InvoicePerformance to get active server stats
        performanceManager.setup(true)

        // Setup chunk saving
        chunkSavingThread.scheduleAtFixedRate({ instanceContainer.saveChunksToStorage() }, 5, 5, TimeUnit.MINUTES)
        Runtime.getRuntime().addShutdownHook(Thread {
            instanceContainer.saveChunksToStorage()
        })

        instanceContainer.setGenerator { unit ->
            unit.modifier().fillHeight(39, 40, Block.STONE)
        }

        // Setup commands
        setupCommands()
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

        logger.info("Successfully registered all Commands!")
    }

    fun getResourceAsString(resource: String): String {
        val streamResource = getResourceAsStream(resource) ?: return ""
        return streamResource.reader().readText()
    }

    fun getResourceAsStream(resource: String): InputStream? = javaClass.getResourceAsStream("/$resource")
    fun getResourceAsJson(resource: String): JsonObject = gson.fromJson(getResourceAsString(resource), JsonObject::class.java)

    fun broadcast(message: Component) = Audiences.all().sendMessage(message)
    fun broadcast(message: Component, predicate: Predicate<Player>) = Audiences.players(predicate).sendMessage(message)

    private fun CommandManager.registerAll(vararg commands: Command) {
        for (command in commands) { register(command) }
    }
}