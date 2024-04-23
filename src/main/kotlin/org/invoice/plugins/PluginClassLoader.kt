package org.invoice.plugins

import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import java.io.File
import java.io.IOException
import java.net.URLClassLoader
import java.util.jar.JarFile

class PluginClassLoader private constructor(private val jar: File) {
    private val urls = arrayOf(jar.toURI().toURL())
    private val classLoader = URLClassLoader.newInstance(urls, javaClass.classLoader)
    val info: PluginTOML by lazy { getPluginTOML() }

    companion object {
        @JvmStatic
        fun create(path: String): PluginClassLoader = create(File(path))

        @JvmStatic
        fun create(file: File): PluginClassLoader = PluginClassLoader(file)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    internal fun registerAllClasses(): List<Class<*>> {
        val classes: MutableList<Class<*>> = mutableListOf()

        val jarFile = JarFile(jar)
        val entries = jarFile.entries()

        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val name = entry.name
            if (name.endsWith(".class")) {
                val className = name.removeSuffix(".class").replace('/', '.')
                val loadedClass = classLoader.loadClass(className)

                classes.add(loadedClass)
            }
        }

        return classes
    }

    private fun getPluginTOML(): PluginTOML {
        val mapper = tomlMapper {}
        val toml = classLoader.getResourceAsStream("plugin.toml")
            ?: throw IOException("Failed to find plugin.toml in ${jar.name}")

        return mapper.decode<PluginTOML>(toml.bufferedReader().use { it.readText() })
    }
}