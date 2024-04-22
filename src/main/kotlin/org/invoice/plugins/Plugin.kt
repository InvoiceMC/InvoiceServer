package org.invoice.plugins

open class Plugin constructor(val name: String, val description: String = "", val version: String = "0.0.1", val authors: Array<out String> = arrayOf()) {
    open fun onEnable() {}
    open fun onDisable() {}
}