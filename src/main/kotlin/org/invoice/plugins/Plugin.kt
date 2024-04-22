package org.invoice.plugins

import java.io.InputStream

open class Plugin @JvmOverloads constructor(val name: String, val description: String = "", val version: String = "0.0.1", val authors: Array<out String> = arrayOf()) {
    open fun onEnable() {}
    open fun onDisable() {}

    fun getResource(resource: String): InputStream? = javaClass.getResourceAsStream(resource)
}