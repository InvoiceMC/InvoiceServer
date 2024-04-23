package org.invoice.plugins

data class PluginTOML(
    val name: String,
    val description: String,
    val version: String,
    val authors: List<String>,
    val mainClass: String,
)
