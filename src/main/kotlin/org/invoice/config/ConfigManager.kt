package org.invoice.config

import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import org.invoice.plugins.PluginTOML
import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException

/**
 * ConfigManager class for plugins to use.
 * Only supports TOML files!
 *
 * @author Azuyamat
 * @since 0.0.1
 */
class ConfigManager (
    path: String
) {
    val file: File = File(path)

    init {
        if (!path.endsWith(".toml")) {
            throw IllegalArgumentException("ConfigManager `path` must be a `.toml` file path.")
        }
        if (!file.exists()) {
            file.mkdirs()
            file.createNewFile()
        }
    }

    inner class Structured<T: Config> {
        inline fun <reified E: Class<T>> getConfig(): E {
            val mapper = tomlMapper {}
            val stream = file.inputStream()
            return mapper.decode<E>(stream.bufferedReader().use { it.readText() })
        }
    }

    inner class UnStructured {
        fun getConfig(): Map<String, Any> {
            val mapper = tomlMapper {}
            val stream = file.inputStream()
            return mapper.decode<Map<String, Any>>(stream.bufferedReader().use { it.readText() })
        }
    }
}