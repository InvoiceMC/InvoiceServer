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

    fun getConfig(): Map<String, Any> {
        return getConfig<Map<String, Any>>()
    }

    inline fun <reified T: Any> getConfig(): T {
        val mapper = tomlMapper {}
        val stream = file.inputStream()
        return mapper.decode<T>(stream.bufferedReader().use { it.readText() })
    }
}