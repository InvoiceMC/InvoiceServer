package org.invoice.plugins

import java.io.File
import java.io.IOException
import java.net.URLClassLoader
import java.util.jar.JarFile

class PluginClassLoader private constructor(private val jar: File) {
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

        val urls = arrayOf(jar.toURI().toURL())
        val classLoader = URLClassLoader.newInstance(urls, javaClass.classLoader)

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
}